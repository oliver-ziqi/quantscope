package com.developer.quantscope;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ziqi
 */

@SpringBootApplication
@MapperScan("com.developer.quantscope.mapper")
@EnableDubbo
public class QuantscopeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuantscopeApplication.class, args);
    }

}
