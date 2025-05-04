package com.ayd.employee_service.parameters.ports;

import com.ayd.employee_service.parameters.models.Parameter;
import com.ayd.employee_service.shared.exceptions.NotFoundException;

public interface ForParameterPort {

    public Parameter findParameterByKey(String key) throws NotFoundException;

}
