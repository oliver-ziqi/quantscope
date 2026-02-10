package com.developer.quantscoperagservice;

import com.developer.quantscoperagservice.config.RagMqProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@MapperScan("com.developer.quantscoperagservice.mapper")
@EnableConfigurationProperties(RagMqProperties.class)
public class QuantscopeRagServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuantscopeRagServiceApplication.class, args);
	}

}
