package com.ayd.config_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.shared.handlers.GlobalExceptionHandler;
import com.ayd.shared.security.AppProperties;
import com.ayd.shared.security.SecurityConfig;
import com.ayd.shared.swagger.SwaggerConfig;



@SpringBootApplication
@EnableJpaAuditing
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class, SecurityConfig.class })
@EnableConfigurationProperties(AppProperties.class)
public class ConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServiceApplication.class, args);
	}

}
