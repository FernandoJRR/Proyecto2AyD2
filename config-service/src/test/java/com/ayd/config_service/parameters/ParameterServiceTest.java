package com.ayd.config_service.parameters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

    @BeforeEach
    void setUp() {
        parameter = new Parameter();
        parameter.setId("param-1");
        parameter.setParameterKey("some_key");
        parameter.setValue("original");
    }

    // -------------------------------
    // findParameterByKey
    // -------------------------------

    @Test
    void findParameterByKey_Success() throws NotFoundException {
        when(parameterRepository.findOneByParameterKey("some_key")).thenReturn(Optional.of(parameter));

        Parameter result = parameterService.findParameterByKey("some_key");

        assertNotNull(result);
        assertEquals("original", result.getValue());
        verify(parameterRepository).findOneByParameterKey("some_key");
    }

    @Test
    void findParameterByKey_NotFound_ThrowsException() {
        when(parameterRepository.findOneByParameterKey("missing_key")).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () ->
                parameterService.findParameterByKey("missing_key"));

        assertEquals("El parametro con la llave ingresada no existe", ex.getMessage());
    }

    // -------------------------------
    // updateRegimenEmpresa
    // -------------------------------

    @Test
    void updateRegimenEmpresa_WithGen_Success() throws Exception {
        parameter.setParameterKey("regimen_empresa");

        when(parameterRepository.findOneByParameterKey("regimen_empresa")).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateRegimenEmpresa("gen");

        assertEquals("{name: \"gen\", \"value\": 12}", result.getValue());
    }

    @Test
    void updateRegimenEmpresa_WithPeq_Success() throws Exception {
        parameter.setParameterKey("regimen_empresa");

        when(parameterRepository.findOneByParameterKey("regimen_empresa")).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateRegimenEmpresa("peq");

        assertEquals("{name: \"peq\", \"value\": 5}", result.getValue());
    }

    @Test
    void updateRegimenEmpresa_InvalidValue_ThrowsException() {
        parameter.setParameterKey("regimen_empresa");

        when(parameterRepository.findOneByParameterKey("regimen_empresa")).thenReturn(Optional.of(parameter));

        InvalidParameterException ex = assertThrows(InvalidParameterException.class, () ->
                parameterService.updateRegimenEmpresa("xyz"));

        assertEquals("El regimen ingresado no es valido.", ex.getMessage());
    }

    @Test
    void updateRegimenEmpresa_NotFound_ThrowsException() {
        when(parameterRepository.findOneByParameterKey("regimen_empresa")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateRegimenEmpresa("gen"));
    }

    // -------------------------------
    // updateNITEmpresa
    // -------------------------------

    @Test
    void updateNITEmpresa_Success() throws Exception {
        parameter.setParameterKey("nit");

        when(parameterRepository.findOneByParameterKey("nit")).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateNITEmpresa("123456-7");

        assertEquals("123456-7", result.getValue());
    }

    @Test
    void updateNITEmpresa_NotFound_ThrowsException() {
        when(parameterRepository.findOneByParameterKey("nit")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateNITEmpresa("123456"));
    }

    // -------------------------------
    // updateNombreEmpresa
    // -------------------------------

    @Test
    void updateNombreEmpresa_Success() throws Exception {
        parameter.setParameterKey("nombre_empresa");

        when(parameterRepository.findOneByParameterKey("nombre_empresa")).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(any(Parameter.class))).thenAnswer(i -> i.getArgument(0));

        Parameter result = parameterService.updateNombreEmpresa("Mi Empresa S.A.");

        assertEquals("Mi Empresa S.A.", result.getValue());
    }

    @Test
    void updateNombreEmpresa_NotFound_ThrowsException() {
        when(parameterRepository.findOneByParameterKey("nombre_empresa")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parameterService.updateNombreEmpresa("Nombre"));
    }
}
