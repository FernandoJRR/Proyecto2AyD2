package com.ayd.config_service.parameters.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.parameters.ports.ForParameterPort;
import com.ayd.config_service.parameters.repositories.ParameterRepository;
import com.ayd.config_service.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ParameterService implements ForParameterPort {

    private final ParameterRepository parameterRepository;

    public Parameter findParameterByKey(String key) throws NotFoundException {
        Optional<Parameter> parameterOptional = parameterRepository.findOneByParameterKey(key);

        if (parameterOptional.isEmpty()) {
            throw new NotFoundException("El parametro con la llave ingresada no existe");
        }

        return parameterOptional.get();
    }
}
