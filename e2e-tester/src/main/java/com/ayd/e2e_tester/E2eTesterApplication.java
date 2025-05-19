package com.ayd.e2e_tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ayd.shared.security.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class E2eTesterApplication {

	public static void main(String[] args) {
		SpringApplication.run(E2eTesterApplication.class, args);
	}

}
