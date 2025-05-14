package com.ayd.reservation_service.schedule.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ayd.reservation_service.schedule.dtos.CreateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.dtos.UpdateScheduleRequestDTO;
import com.ayd.reservation_service.schedule.models.Schedule;
import com.ayd.reservation_service.schedule.repositories.ScheduleRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

class ScheduleServiceTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        scheduleService = new ScheduleService(scheduleRepository);
    }

    @Test
    void getScheduleById_shouldReturnSchedule_whenExists() throws NotFoundException {
        Schedule schedule = new Schedule();
        when(scheduleRepository.findById("123")).thenReturn(Optional.of(schedule));

        Schedule result = scheduleService.getScheduleById("123");

        assertEquals(schedule, result);
    }

    @Test
    void getScheduleById_shouldThrowNotFoundException_whenNotExists() {
        when(scheduleRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> scheduleService.getScheduleById("123"));
    }

    @Test
    void getSchedulesByOnline_shouldReturnSchedules() {
        List<Schedule> expected = List.of(new Schedule());
        when(scheduleRepository.findAll()).thenReturn(expected);

        List<Schedule> result = scheduleService.getAllSchedules();

        assertEquals(expected, result);
    }

    @Test
    void createSchedule_shouldCreateAndReturnSchedule_whenNoConflictExists() throws DuplicatedEntryException {
        CreateScheduleRequestDTO dto = new CreateScheduleRequestDTO(LocalTime.of(10, 0), LocalTime.of(11, 0));
        Schedule expected = new Schedule(dto);
        when(scheduleRepository.existsByStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime()))
                .thenReturn(false);
        when(scheduleRepository.save(any(Schedule.class))).thenReturn(expected);

        Schedule result = scheduleService.createSchedule(dto);

        assertEquals(expected, result);
    }

    @Test
    void createSchedule_shouldThrowDuplicatedEntryException_whenConflictExists() {
        CreateScheduleRequestDTO dto = new CreateScheduleRequestDTO(LocalTime.of(10, 0), LocalTime.of(11, 0));
        when(scheduleRepository.existsByStartTimeAndEndTime(dto.getStartTime(), dto.getEndTime()))
                .thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> scheduleService.createSchedule(dto));
    }

    @Test
    void updateSchedule_shouldUpdateAndReturnSchedule_whenNoConflictExists()
            throws NotFoundException, DuplicatedEntryException {
        String id = "schedule-123";
        Schedule existing = new Schedule();
        UpdateScheduleRequestDTO dto = new UpdateScheduleRequestDTO(LocalTime.of(12, 0), LocalTime.of(13, 0));
        Schedule updated = new Schedule(dto.getStartTime(), dto.getEndTime());

        when(scheduleRepository.findById(id)).thenReturn(Optional.of(existing));
        when(scheduleRepository.existsByStartTimeAndEndTimeAndIdNot(dto.getStartTime(), dto.getEndTime(), id)).thenReturn(false);
        when(scheduleRepository.save(existing)).thenReturn(updated);

        Schedule result = scheduleService.updateSchedule(id, dto);

        assertEquals(updated.getStartTime(), result.getStartTime());
        assertEquals(updated.getEndTime(), result.getEndTime());
    }

    @Test
    void updateSchedule_shouldThrowDuplicatedEntryException_whenConflictExists() {
        String id = "schedule-123";
        UpdateScheduleRequestDTO dto = new UpdateScheduleRequestDTO(LocalTime.of(12, 0), LocalTime.of(13, 0));
        when(scheduleRepository.findById(id)).thenReturn(Optional.of(new Schedule()));
        when(scheduleRepository.existsByStartTimeAndEndTimeAndIdNot(dto.getStartTime(), dto.getEndTime(),
                id)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> scheduleService.updateSchedule(id, dto));
    }

    @Test
    void updateSchedule_shouldThrowNotFoundException_whenNotFound() {
        when(scheduleRepository.findById("invalid")).thenReturn(Optional.empty());
        UpdateScheduleRequestDTO dto = new UpdateScheduleRequestDTO(LocalTime.NOON, LocalTime.MIDNIGHT);

        assertThrows(NotFoundException.class, () -> scheduleService.updateSchedule("invalid", dto));
    }

    @Test
    void deleteSchedule_shouldDelete_whenExists() throws NotFoundException {
        Schedule schedule = new Schedule();
        when(scheduleRepository.findById("123")).thenReturn(Optional.of(schedule));

        boolean result = scheduleService.deleteSchedule("123");

        assertTrue(result);
        verify(scheduleRepository).delete(schedule);
    }

    @Test
    void deleteSchedule_shouldThrowNotFoundException_whenNotExists() {
        when(scheduleRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> scheduleService.deleteSchedule("123"));
    }
}
