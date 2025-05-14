package com.ayd.reservation_service.schedule.ports;

import java.util.List;

import com.ayd.reservation_service.schedule.dtos.CreateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.dtos.UpdateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.models.Schedule;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

public interface ForSchedulePort {
    public Schedule getScheduleById(String scheduleId) throws NotFoundException;

    public List<Schedule> getAllSchedules();

    public Schedule createSchedule(CreateScheduleRequestDTO createScheduleRequest)
            throws DuplicatedEntryException, IllegalStateException;

    public Schedule updateSchedule(String scheduleId, UpdateScheduleRequestDTO updateScheduleRequest)
            throws NotFoundException, DuplicatedEntryException, IllegalStateException;

    public boolean deleteSchedule(String scheduleId) throws NotFoundException;
}
