package com.developer.quantscopegateway.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ziqi
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "quantscope.gateway")
public class NoAuthPaths {
    private List<String> noAuthPaths = new ArrayList<>();
}