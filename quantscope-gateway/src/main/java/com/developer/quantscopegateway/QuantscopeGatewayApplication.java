package com.developer.quantscopegateway;



import com.developer.quantscopegateway.config.NoAuthPaths;
import com.developer.quantscopegateway.config.RateLimitProperties;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author ziqi
 */

@SpringBootApplication
@EnableConfigurationProperties({NoAuthPaths.class, RateLimitProperties.class})
@EnableDubbo
@Service
public class QuantscopeGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantscopeGatewayApplication.class, args);
    }

}
