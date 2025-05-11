package com.ayd.reservation_service.schedule.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.reservation_service.schedule.dtos.ScheduleResponseDTO;
import com.ayd.reservation_service.schedule.models.Schedule;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {
    public ScheduleResponseDTO formScheduleToScheduleResponseDTO(Schedule schedule);

    public List<ScheduleResponseDTO> formScheduleListToScheduleResponseDTOList(List<Schedule> scheduleList);
}
