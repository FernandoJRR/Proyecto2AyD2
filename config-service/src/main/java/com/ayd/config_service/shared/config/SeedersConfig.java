package com.ayd.config_service.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ayd.config_service.parameters.enums.ParameterEnum;
import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.parameters.repositories.ParameterRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("dev || prod || local")
@RequiredArgsConstructor
@Component
public class SeedersConfig implements CommandLineRunner {

	private final ParameterRepository parameterRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {
		System.out.println("Ejecutnado el metodo de seeders.");
		System.out.println("Creando los seeders.");
		System.out.println("Ejecutando seeders.");

        for (ParameterEnum parameterEnum : ParameterEnum.values()) {
            boolean exists = parameterRepository.existsByParameterKey(parameterEnum.getKey());
            if (!exists) {
                Parameter currentParameter = new Parameter(parameterEnum.getKey(), parameterEnum.getDefaultValue(), parameterEnum.getName());
                parameterRepository.save(currentParameter);
            }
        }
	}
}
