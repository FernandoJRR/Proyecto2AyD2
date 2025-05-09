package com.ayd.config_service.parameters.ports;

import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.shared.exceptions.NotFoundException;

public interface ForParameterPort {

    public Parameter findParameterByKey(String key) throws NotFoundException;

}
