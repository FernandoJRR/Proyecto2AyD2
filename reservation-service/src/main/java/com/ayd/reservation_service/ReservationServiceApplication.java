package com.ayd.reservation_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.shared.handlers.GlobalExceptionHandler;
import com.ayd.shared.swagger.SwaggerConfig;

@SpringBootApplication
@EnableJpaAuditing
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class})
public class ReservationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationServiceApplication.class, args);
	}

}
