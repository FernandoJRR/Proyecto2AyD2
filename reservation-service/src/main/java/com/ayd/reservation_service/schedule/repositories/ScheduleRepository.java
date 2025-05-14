package com.ayd.reservation_service.schedule.repositories;

import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.reservation_service.schedule.models.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    boolean existsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);

    boolean existsByStartTimeAndEndTimeAndIdNot(LocalTime startTime, LocalTime endTime,
            String id);


}
