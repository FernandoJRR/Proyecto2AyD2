package com.ayd.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.shared.swagger.SwaggerConfig;
import com.ayd.shared.handlers.GlobalExceptionHandler;
import com.ayd.shared.security.AppProperties;
import com.ayd.shared.security.SecurityConfig;

@SpringBootApplication
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class, SecurityConfig.class })
@EnableConfigurationProperties(AppProperties.class)
@EnableJpaAuditing
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
