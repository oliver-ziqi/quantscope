package com.developer.quantscope;

import com.developer.quantscope.model.network.BaseResponse;
import com.developer.quantscope.model.vo.ApiKeyResponse;
import com.developer.quantscopecommen.util.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ziqi
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GatewayTest {

    @Autowired
    private WebTestClient webTestClient;

    String testUserAccount = "defaultuser8";
    String testUserPassword = "defaultpass8";

    String accessKey = null;
    String secretKey = null;

    String token = null;
    @BeforeAll
    void setup() {
        this.webTestClient = WebTestClient
            .bindToServer()
            .baseUrl("http://localhost:8090") // Gateway port
            .build();
    }

    @Test
    @Order(1)
    public void registerTest() {
        // 1. Simulate user registration
        Map<String, String> registerBody = new HashMap<>();
        registerBody.put("userAccount", testUserAccount);
        registerBody.put("userPassword", testUserPassword);

        webTestClient.post().uri("/api/user/register")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(registerBody), Map.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(response -> {
                System.out.println("Registration response: " + response.getResponseBody());
            });

    }

    @Test
    @Order(2)
    public void loginTest() {
        // 2. Simulate user login (API Key is not directly obtained from login response now, but login operation may still be required)
        Map<String, String> loginBody = new HashMap<>();
        loginBody.put("userAccount", testUserAccount);
        loginBody.put("userPassword", testUserPassword);

        BaseResponse<String> responseBody = webTestClient.post().uri("/api/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(loginBody), Map.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<BaseResponse<String>>() {
            })
            .consumeWith(response -> {
                System.out.println("Login response: " + response.getResponseBody());
            })
            .returnResult()
            .getResponseBody();

        // Simulate front-end cookie storage
        token = responseBody.getData();
    }

    @Test
    @Order(3)
    public void getAPiKeyTest() {

        // 2.1. Call getApiKey interface to get API Key
        BaseResponse<ApiKeyResponse> response =
            webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/user/getApiKey")
                    .queryParam("userAccount", testUserAccount)
                    .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<BaseResponse<ApiKeyResponse>>() {
                })
                .returnResult()
                .getResponseBody();

        ApiKeyResponse apiKeyResponse = response.getData();
        System.out.println("API Key response: " + apiKeyResponse);

        accessKey = apiKeyResponse.ak();
        secretKey = apiKeyResponse.sk();
        log.info("accessKey is {} , secretKey is{}", accessKey, secretKey);
    }

    @Test
    @Order(4)
    public void routingTest() {
        // 3. Use the obtained API Key to test the authenticated interface
        // Assume there is a /api/interface/list interface that requires authentication
        // To simplify, an empty body is used here, in actual application, the body can be constructed according to the interface requirements
        String requestBody = "{}";
        long timestamp = System.currentTimeMillis() / 1000;
        log.info("accessKey is {} , secretKey is{}", accessKey, secretKey);
        String sign = SignUtils.genSign(requestBody, secretKey); // 需要引入 SignUtils

        webTestClient.get().uri("/api/interface/list")
            .header("accessKey", accessKey)
            .header("nonce", "12345") // Random number, simplified to a fixed value here
            .header("timestamp", String.valueOf(timestamp))
            .header("sign", sign)
            .header("body", requestBody)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .consumeWith(response -> {
                System.out.println("Authenticated interface response: " + response.getResponseBody());
            });
    }
}
