package com.ayd.reservation_service.schedule.repositories;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.reservation_service.schedule.models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    List<Schedule> findByOnline(boolean online);

    List<Schedule> findByOnlineAndStartTimeBetween(boolean online, LocalTime startTime, LocalTime endTime);

    boolean existsByOnlineAndStartTimeBetween(boolean online, LocalTime startTime, LocalTime endTime);

    boolean existsByOnlineAndStartTimeBetweenAndIdNot(boolean online, LocalTime startTime, LocalTime endTime,
            String id);
}
