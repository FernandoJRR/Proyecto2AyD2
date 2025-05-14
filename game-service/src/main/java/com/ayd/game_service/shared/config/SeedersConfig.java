package com.ayd.game_service.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ayd.game_service.holes.models.Hole;
import com.ayd.game_service.holes.repositories.HoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("dev || prod || local")
@RequiredArgsConstructor
@Component
public class SeedersConfig implements CommandLineRunner {

	private final HoleRepository holeRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {
		System.out.println("Ejecutnado el metodo de seeders.");
		if (holeRepository.count() > 0) {
			return;
		}

		System.out.println("Creando los seeders.");

        for (int i = 1; i <= 18; i++) {
            Hole hole = new Hole();
            hole.setNumber(i);
            hole.setName("Hoyo " + i);
            holeRepository.save(hole);
        }
	}
}
