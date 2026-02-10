package com.developer.quantscopegateway;

import com.developer.quantscopecommon.entity.InterfaceInfo;
import com.developer.quantscopecommon.entity.User;
import com.developer.quantscopecommon.service.InnerInterfaceInfoService;
import com.developer.quantscopecommon.service.InnerUserInterfaceInfoService;
import com.developer.quantscopecommon.service.InnerUserService;
import com.developer.quantscopecommon.util.SignUtils;
import com.developer.quantscopegateway.config.NoAuthPaths;
import com.developer.quantscopegateway.config.RateLimitProperties;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author ziqi
 */

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    private static final String INTERFACE_HOST = "http://localhost:8123";

    private NoAuthPaths noAuthPaths;

    private final RedissonClient redissonClient;
    private final RateLimitProperties rateLimitProperties;

    public CustomGlobalFilter(NoAuthPaths noAuthPaths,
                              RedissonClient redissonClient,
                              RateLimitProperties rateLimitProperties) {
        this.noAuthPaths = noAuthPaths;
        this.redissonClient = redissonClient;
        this.rateLimitProperties = rateLimitProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1. request log
        ServerHttpRequest request = exchange.getRequest();
        String path = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        log.info("request path is {}, method is {}", path, method);

        // If it's a no-authentication path, let it pass directly
        if (noAuthPaths.getNoAuthPaths().contains(request.getPath().value())) {
            return chain.filter(exchange);
        }

        //2. white list
        ServerHttpResponse response = exchange.getResponse();
        String hostAdr = request.getLocalAddress().getHostString();
        if (!IP_WHITE_LIST.contains(hostAdr)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        //3. check the api request
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");

        //3.1 get secretKey
        User invokeUser = innerUserService.getInvokeUser(accessKey);
        String secretKey = invokeUser.getSecretKey();

        //3.2 combine sk and body
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        log.info("Verification passed");

        //4. check whether the api is existing
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        log.info("path is {}, method is{}", path, method);

        if (interfaceInfo == null) {
            return handleNoAuth(response);
        }

        //6. rate limit by apiKeyId + apiId
        if (rateLimitProperties.isEnabled()) {
            boolean allowed = acquireRateLimit(invokeUser.getId(), interfaceInfo.getId());
            if (!allowed) {
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return response.setComplete();
            }
        }

        //5. continue other filter
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceInfoId, Long userId) {

        // decorate the response, take the data and put it back
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        HttpStatusCode statusCode = originalResponse.getStatusCode();

        // Anonymous inner class + decorator pattern
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                log.info("body instanceof Flux: {}", (body instanceof Flux));
                if (body instanceof Flux) {

                    // transform publisher to fluxBody, fluxBody includes some flux operations
                    Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                    // write back data
                    return super.writeWith(
                        fluxBody.map(dataBuffer -> {

                            // if success, api count plus one
                            if (statusCode == HttpStatus.OK) {
                                try {
                                    innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                } catch (Exception e) {
                                    log.error("invokeCount error", e);
                                }
                            }

                            // read data to content array and avoid memory leaks
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            DataBufferUtils.release(dataBuffer);

                            // record calling response to log
                            StringBuilder logString = new StringBuilder(200);
                            List<Object> rspArgs = new ArrayList<>();
                            rspArgs.add(originalResponse.getStatusCode());
                            String data = new String(content, StandardCharsets.UTF_8);
                            logString.append(data);

                            log.info("response data: " + data);
                            return bufferFactory.wrap(content);
                        }));
                } else {
                    log.error("<--- {} response error", getStatusCode());
                }
                //
                return super.writeWith(body);
            }
        };

        // set response to decorated response
        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private boolean acquireRateLimit(Long apiKeyId, Long apiId) {
        String key = rateLimitProperties.getKeyPrefix() + ":" + apiKeyId + ":" + apiId;
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);
        rateLimiter.trySetRate(
            RateType.OVERALL,  // all clients use one limiter
            rateLimitProperties.getPermits(), // how many requests in one interval
            rateLimitProperties.getIntervalSeconds(), // how long one term
            RateIntervalUnit.SECONDS //units
        );
        return rateLimiter.tryAcquire(1);
    }
}
