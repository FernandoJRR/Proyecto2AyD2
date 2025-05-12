package com.ayd.config_service.parameters.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ayd.config_service.parameters.enums.ParameterEnum;
import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.parameters.ports.ForParameterPort;
import com.ayd.config_service.parameters.repositories.ParameterRepository;
import com.ayd.shared.exceptions.*;

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

    public Parameter updateRegimenEmpresa(String newRegimen) throws NotFoundException, InvalidParameterException {
        Parameter parameterRegimen = parameterRepository.findOneByParameterKey(ParameterEnum.REGIMEN_EMPRESA.getKey())
            .orElseThrow(() -> new NotFoundException("El parametro con la llave ingresada no existe"));

        switch (newRegimen) {
            case "gen":
                parameterRegimen.setValue("{name: \"gen\", \"value\": 12}");
                break;
            case "peq":
                parameterRegimen.setValue("{name: \"peq\", \"value\": 5}");
                break;
            default:
                throw new InvalidParameterException("El regimen ingresado no es valido.");
        }

        return parameterRepository.save(parameterRegimen);
    }

    public Parameter updateNITEmpresa(String newNIT) throws NotFoundException, InvalidParameterException {
        Parameter parameterNIT = parameterRepository.findOneByParameterKey(ParameterEnum.NIT_EMPRESA.getKey())
            .orElseThrow(() -> new NotFoundException("El parametro con la llave ingresada no existe"));

        parameterNIT.setValue(newNIT);

        return parameterRepository.save(parameterNIT);
    }

    public Parameter updateNombreEmpresa(String newNombre) throws NotFoundException, InvalidParameterException {
        Parameter parameterNombre = parameterRepository.findOneByParameterKey(ParameterEnum.NOMBRE_EMPRESA.getKey())
            .orElseThrow(() -> new NotFoundException("El parametro con la llave ingresada no existe"));

        parameterNombre.setValue(newNombre);

        return parameterRepository.save(parameterNombre);
    }

    public Parameter updateDiasVacaciones(String newDays) throws NotFoundException, InvalidParameterException {
        Parameter parameterDias = parameterRepository.findOneByParameterKey(ParameterEnum.DIAS_VACACIONES.getKey())
            .orElseThrow(() -> new NotFoundException("El parametro con la llave ingresada no existe"));

        parameterDias.setValue(newDays);

        return parameterRepository.save(parameterDias);
    }
}
