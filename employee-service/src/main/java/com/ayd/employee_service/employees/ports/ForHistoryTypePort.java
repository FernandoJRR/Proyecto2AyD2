package com.ayd.employee_service.employees.ports;

import java.util.List;

import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.shared.exceptions.*;

public interface ForHistoryTypePort {
    public HistoryType findHistoryTypeByName(String historyTypeName) throws NotFoundException;

    public HistoryType findHistoryTypeById(String historyTypeId) throws NotFoundException;

    public List<HistoryType> findDeactivationHistoryTypes();

    public List<HistoryType> findAll();
}
