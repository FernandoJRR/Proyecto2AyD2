package com.ayd.employee_service.parameters.ports;

import com.ayd.employee_service.parameters.models.Parameter;
import com.ayd.shared.exceptions.*;

public interface ForParameterPort {

    public Parameter findParameterByKey(String key) throws NotFoundException;

}
