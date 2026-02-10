package com.developer.quantscope.aop;

import com.developer.quantscope.exception.BusinessException;
import com.developer.quantscope.mapper.PermissionMapper;
import com.developer.quantscope.mapper.UserMapper;
import com.developer.quantscope.model.network.ErrorCode;
import com.developer.quantscope.util.JwtService;
import com.developer.quantscopecommon.entity.Permission;
import com.developer.quantscopecommon.entity.User;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

/**
 * RBAC + tenant isolation aspect.
 */
@Slf4j
@Aspect
@Component
public class RbacAspect {

    private static final String HEADER_AUTH = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String TRACE_ID = "traceId";

    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;
    private final TenantResolver tenantResolver;

    public RbacAspect(JwtService jwtService,
                      UserMapper userMapper,
                      PermissionMapper permissionMapper,
                      TenantResolver tenantResolver) {
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.permissionMapper = permissionMapper;
        this.tenantResolver = tenantResolver;
    }

    @Around("@annotation(requirePermission) || @within(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission)
        throws Throwable {

        ServerWebExchange exchange = resolveExchange(joinPoint.getArgs());
        if (exchange == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ServerWebExchange is required for RBAC check");
        }

        String requestId = resolveRequestId(exchange.getRequest().getHeaders());
        MDC.put(TRACE_ID, requestId);
        try {
            logBasic(exchange, requestId);

            HttpHeaders headers = exchange.getRequest().getHeaders();
            Long requestTenantId = tenantResolver.resolveTenantId(headers);

            if (requirePermission.requireTenant() && requestTenantId == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Missing tenant id");
            }

            String auth = headers.getFirst(HEADER_AUTH);
            if (!StringUtils.hasText(auth) || !auth.startsWith(BEARER_PREFIX)) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
            }

            String rawToken = auth.substring(BEARER_PREFIX.length());
            Optional<Long> userIdOptional = jwtService.getUserIdFromToken(rawToken);
            if (userIdOptional.isEmpty()) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
            }

            Long userId = userIdOptional.get();
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
            }

            if (requirePermission.requireTenant()) {
                if (user.getTenantId() == null || !Objects.equals(user.getTenantId(), requestTenantId)) {
                    throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "Tenant mismatch");
                }
            }

            String required = requirePermission.value();
            if (StringUtils.hasText(required)) {
                List<Permission> permissions = requirePermission.requireTenant()
                    ? permissionMapper.selectPermissionsByUserIdAndTenantId(userId, requestTenantId)
                    : permissionMapper.selectPermissionsByUserId(userId);
                Set<String> codes = permissions.stream()
                    .map(Permission::getCode)
                    .filter(StringUtils::hasText)
                    .collect(Collectors.toSet());
                if (!codes.contains(required)) {
                    throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
                }
            }

            return joinPoint.proceed();
        } finally {
            MDC.remove(TRACE_ID);
        }
    }

    private ServerWebExchange resolveExchange(Object[] args) {
        if (args == null) {
            return null;
        }
        for (Object arg : args) {
            if (arg instanceof ServerWebExchange) {
                return (ServerWebExchange) arg;
            }
        }
        return null;
    }

    private String resolveRequestId(HttpHeaders headers) {
        String requestId = headers.getFirst("X-Request-Id");
        if (!StringUtils.hasText(requestId)) {
            requestId = headers.getFirst("X-Trace-Id");
        }
        if (!StringUtils.hasText(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private void logBasic(ServerWebExchange exchange, String requestId) {
        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        log.info("rbac requestId={} method={} path={}", requestId, method, path);
    }
}
