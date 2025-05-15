package com.ayd.invoice_service.Invoice.ports;

import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;

public interface EmployeeClientPort {
    public EmployeeResponseDTO findEmployeeByUserName(String userName) throws NotFoundException;
}
