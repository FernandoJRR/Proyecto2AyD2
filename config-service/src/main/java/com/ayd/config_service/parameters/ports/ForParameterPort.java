package com.ayd.config_service.parameters.ports;

import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.shared.exceptions.*;

public interface ForParameterPort {

    public Parameter findParameterByKey(String key) throws NotFoundException;

    public Parameter updateRegimenEmpresa(String newRegimen) throws NotFoundException, InvalidParameterException;
    public Parameter updateNITEmpresa(String newNIT) throws NotFoundException, InvalidParameterException;
    public Parameter updateNombreEmpresa(String newNombre) throws NotFoundException, InvalidParameterException;
}
