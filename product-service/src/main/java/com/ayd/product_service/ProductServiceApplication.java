package com.ayd.product_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.shared.handlers.GlobalExceptionHandler;

@SpringBootApplication
@Import(GlobalExceptionHandler.class)
@EnableJpaAuditing
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

}
