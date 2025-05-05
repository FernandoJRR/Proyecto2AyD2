package com.ayd.employee_service.employees.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ayd.employee_service.employees.dtos.EmployeeHistoryDateRequestDTO;
import com.ayd.employee_service.employees.dtos.EmployeeHistoryResponseDTO;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.shared.utils.DateFormatterUtil;

@Mapper(componentModel = "spring", uses = { DateFormatterUtil.class })
public interface EmployeeHistoryMapper {
    public EmployeeHistory fromEmployeeHistoryDateRequestDtoToEmployeeHistory(EmployeeHistoryDateRequestDTO dto);

    @Mapping(source = "historyDate", target = "historyDate", qualifiedByName = "formatDateToLocalFormat")
    public EmployeeHistoryResponseDTO fromEmployeeHistoryToEmployeeHistoryDto(EmployeeHistory employeeHistory);

    public List<EmployeeHistoryResponseDTO> fromEmployeeHistoriesToEmployeeHistoryDtoList(
            List<EmployeeHistory> employeeHistories);
}
