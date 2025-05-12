package com.ayd.game_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.ayd.shared.SwaggerConfig;
import com.ayd.shared.handlers.GlobalExceptionHandler;

@SpringBootApplication
@EnableJpaAuditing
@Import({ GlobalExceptionHandler.class, SwaggerConfig.class })
public class GameServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameServiceApplication.class, args);
	}

}
