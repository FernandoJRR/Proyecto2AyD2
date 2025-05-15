package com.ayd.reservation_service.shared.config;

import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ayd.reservation_service.schedule.models.Schedule;
import com.ayd.reservation_service.schedule.repositories.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("dev || prod || local")
@RequiredArgsConstructor
@Component
public class SeedersConfig implements CommandLineRunner {

	private final ScheduleRepository scheduleRepository;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {
		System.out.println("Ejecutnado el metodo de seeders.");
		if (scheduleRepository.count() == 0) {
			for (int i = 8; i < 20; i++) {
				LocalTime start = LocalTime.of(i, 0);
				LocalTime end = LocalTime.of(i + 1, 0);

				Schedule presencial = new Schedule(start, end);


				scheduleRepository.save(presencial);
			}

			System.out.println("24 horarios creados correctamente.");
		} else {
			System.out.println("Ya existen horarios. No se crean duplicados.");
		}

		System.out.println("Creando los seeders.");
	}
}
