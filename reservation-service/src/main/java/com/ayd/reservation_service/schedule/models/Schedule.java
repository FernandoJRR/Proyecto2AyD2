package com.ayd.reservation_service.schedule.models;

import java.time.LocalTime;

import com.ayd.reservation_service.schedule.dtos.CreateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.dtos.UpdateScheduleRequestDTO;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Schedule extends Auditor {
    @Column(nullable = false)
    private LocalTime startTime;
    @Column(nullable = false)
    private LocalTime endTime;


    public Schedule(CreateScheduleRequestDTO createScheduleRequest) {
        this.startTime = createScheduleRequest.getStartTime();
        this.endTime = createScheduleRequest.getEndTime();
    }

    public Schedule update(UpdateScheduleRequestDTO updateScheduleRequest) {
        this.startTime = updateScheduleRequest.getStartTime();
        this.endTime = updateScheduleRequest.getEndTime();
        return this;
    }
}
