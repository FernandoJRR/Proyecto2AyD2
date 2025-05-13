package com.ayd.config_service.parameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.config_service.parameters.models.Parameter;
import com.ayd.config_service.parameters.repositories.ParameterRepository;
import com.ayd.config_service.parameters.services.ParameterService;
import com.ayd.shared.exceptions.InvalidParameterException;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class ParameterServiceTest {
    @Mock
    private ParameterRepository parameterRepository;

    @InjectMocks
    private ParameterService parameterService;

    private Parameter parameter;
    private Parameter nitParameter;

    private String PARAMETER_ID = "fdsc-kcdo-ncds-mcds";
    private String PARAMETER_NIT_ID = "csdm-fdsc-fetr-mcwc";
    private String PARAMETER_KEY = "llave_test";
    private String INVALID_PARAMETER_KEY = "llave_test_invalida";

    private String PARAMETER_REGIMEN_KEY = "regimen_empresa";
    private String PARAMETER_NIT_KEY = "nit_empresa";
    private String PARAMETER_NOMBRE_KEY = "nombre_empresa";
    private String PARAMETER_DIAS_KEY = "dias_vacaciones";

    private String PARAMETER_VALUE = "test";
    private String PARAMETER_NIT_VALUE = "1234567";
    private String PARAMETER_NEW_NIT_VALUE = "654321";

    private String VALUE_REGIMEN_GENERAL = "gen";
    private String VALUE_REGIMEN_PEQUENO = "peq";
    private String VALUE_REGIMEN_INVALID = "noexiste";

    @BeforeEach
    void setUp() {
        parameter = new Parameter();
        parameter.setId(PARAMETER_ID);
        parameter.setParameterKey(PARAMETER_KEY);
        parameter.setValue(PARAMETER_VALUE);

        nitParameter = new Parameter();
        nitParameter.setId(PARAMETER_NIT_ID);
        nitParameter.setParameterKey(PARAMETER_NIT_KEY);
        nitParameter.setValue(PARAMETER_NIT_VALUE);
    }

    @Test
    void findParameterByKey_Success() throws NotFoundException {
        when(parameterRepository.findOneByParameterKey(PARAMETER_KEY)).thenReturn(Optional.of(parameter));

        Parameter result = parameterService.findParameterByKey(PARAMETER_KEY);

        assertNotNull(result);
        assertEquals(PARAMETER_VALUE, result.getValue());
        verify(parameterRepository).findOneByParameterKey(PARAMETER_KEY);
    }

    @Test
    void findParameterByKey_NotFound() {
        when(parameterRepository.findOneByParameterKey(INVALID_PARAMETER_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.findParameterByKey(INVALID_PARAMETER_KEY));
    }

    @Test
    void updateRegimenEmpresa_WithGen_Success() throws NotFoundException, InvalidParameterException {
        parameter.setParameterKey(PARAMETER_REGIMEN_KEY);

        when(parameterRepository.findOneByParameterKey(PARAMETER_REGIMEN_KEY)).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateRegimenEmpresa(VALUE_REGIMEN_GENERAL);

        assertEquals("{\"name\": \"gen\", \"value\": 12}", result.getValue());
    }

    @Test
    void updateRegimenEmpresa_WithPeq_Success() throws NotFoundException, InvalidParameterException {
        parameter.setParameterKey(PARAMETER_REGIMEN_KEY);

        when(parameterRepository.findOneByParameterKey(PARAMETER_REGIMEN_KEY)).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateRegimenEmpresa(VALUE_REGIMEN_PEQUENO);

        assertEquals("{\"name\": \"peq\", \"value\": 5}", result.getValue());
    }

    @Test
    void updateRegimenEmpresa_InvalidValue() {
        parameter.setParameterKey(PARAMETER_REGIMEN_KEY);

        when(parameterRepository.findOneByParameterKey(PARAMETER_REGIMEN_KEY)).thenReturn(Optional.of(parameter));

        assertThrows(InvalidParameterException.class, () ->
                parameterService.updateRegimenEmpresa(VALUE_REGIMEN_INVALID));
    }

    @Test
    void updateRegimenEmpresa_NotFound() {
        when(parameterRepository.findOneByParameterKey(PARAMETER_REGIMEN_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateRegimenEmpresa(VALUE_REGIMEN_GENERAL));
    }

    @Test
    void updateNITEmpresa_Success() throws Exception {
        when(parameterRepository.findOneByParameterKey(PARAMETER_NIT_KEY)).thenReturn(Optional.of(nitParameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateNITEmpresa(PARAMETER_NEW_NIT_VALUE);

        assertEquals(PARAMETER_NEW_NIT_VALUE, result.getValue());
    }

    @Test
    void updateNITEmpresa_NotFound() {
        when(parameterRepository.findOneByParameterKey(PARAMETER_NIT_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateNITEmpresa("123456"));
    }

    @Test
    void updateNombreEmpresa_Success() throws Exception {
        parameter.setParameterKey(PARAMETER_NOMBRE_KEY);

        when(parameterRepository.findOneByParameterKey(PARAMETER_NOMBRE_KEY)).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateNombreEmpresa("Mi Empresa S.A.");

        assertEquals("Mi Empresa S.A.", result.getValue());
    }

    @Test
    void updateNombreEmpresa_NotFound() {
        when(parameterRepository.findOneByParameterKey(PARAMETER_NOMBRE_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateNombreEmpresa("Nombre"));
    }

    @Test
    void updateDiasVacaciones_Success() throws Exception {
        parameter.setParameterKey(PARAMETER_DIAS_KEY);

        when(parameterRepository.findOneByParameterKey(PARAMETER_DIAS_KEY)).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateDiasVacaciones("20");

        assertEquals("20", result.getValue());
    }

    @Test
    void updateDiasVacaciones_NotFound() {
        when(parameterRepository.findOneByParameterKey(PARAMETER_DIAS_KEY)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateDiasVacaciones("20"));
    }
}
