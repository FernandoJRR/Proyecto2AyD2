package com.ayd.config_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.config_service.shared.config.AppProperties;
import com.ayd.config_service.shared.config.SwaggerConfig;
import com.ayd.config_service.shared.utils.GlobalExceptionHandler;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(AppProperties.class)
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class })
public class ConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServiceApplication.class, args);
	}

}
