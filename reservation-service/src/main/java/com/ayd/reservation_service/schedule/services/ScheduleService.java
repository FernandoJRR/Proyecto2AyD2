package com.ayd.reservation_service.schedule.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.reservation_service.schedule.dtos.CreateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.dtos.UpdateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.models.Schedule;
import com.ayd.reservation_service.schedule.ports.ForSchedulePort;
import com.ayd.reservation_service.schedule.repositories.ScheduleRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class ScheduleService implements ForSchedulePort {

    private final ScheduleRepository scheduleRepository;

    @Override
    public Schedule getScheduleById(String scheduleId) throws NotFoundException {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("No se encontró la programación con el ID: " + scheduleId));
    }

    @Override
    public List<Schedule> getSchedulesByOnline(boolean online) {
        return scheduleRepository.findByOnline(online);
    }

    @Override
    public Schedule createSchedule(CreateScheduleRequestDTO createScheduleRequest)
            throws DuplicatedEntryException, IllegalStateException {
        if (scheduleRepository.existsByOnlineAndStartTimeBetween(createScheduleRequest.isOnline(),
                createScheduleRequest.getStartTime(),
                createScheduleRequest.getEndTime())) {
            throw new DuplicatedEntryException("Ya existe una programación con el mismo horario.");
        }
        Schedule schedule = new Schedule(createScheduleRequest);
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(String scheduleId, UpdateScheduleRequestDTO updateScheduleRequest)
            throws NotFoundException, DuplicatedEntryException, IllegalStateException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("No se encontró la programación con el ID: " + scheduleId));
        if (scheduleRepository.existsByOnlineAndStartTimeBetweenAndIdNot(updateScheduleRequest.isOnline(),
                updateScheduleRequest.getStartTime(),
                updateScheduleRequest.getEndTime(), scheduleId)) {
            throw new DuplicatedEntryException("Ya existe una programación con el mismo horario.");
        }
        schedule = schedule.update(updateScheduleRequest);
        return scheduleRepository.save(schedule);
    }

    @Override
    public boolean deleteSchedule(String scheduleId) throws NotFoundException {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("No se encontró la programación con el ID: " + scheduleId));
        scheduleRepository.delete(schedule);
        return true;
    }

}
