package com.ayd.employee_service.employees.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.employee_service.employees.enums.HistoryTypeEnum;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.repositories.HistoryTypeRepository;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class HistoryTypeServiceTest {
    private static final String VALID_TYPE_NAME = "";
    private static final String FIRING_TYPE_ID = "erwf-fdns-fdsc";
    private static final String RESIGN_TYPE_ID = "cmea-cdms-mocd";
    private static final String VALID_TYPE_ID = "id123";
    private static final String NOT_FOUND_MESSAGE_NAME = "No existe el tipo de Historial con el nombre ingresado.";
    private static final String NOT_FOUND_MESSAGE_ID = "No existe el tipo de Historial con el id ingresado.";

    private HistoryType firingHistoryType;
    private HistoryType resignHistoryType;
    private HistoryType genericHistoryType;

    @Mock
    private HistoryTypeRepository historyTypeRepository;

    @InjectMocks
    private HistoryTypeService historyTypeService;

    @BeforeEach
    public void setUp() {
        firingHistoryType = new HistoryType();
        firingHistoryType.setId(FIRING_TYPE_ID);
        firingHistoryType.setType(HistoryTypeEnum.DESPIDO.getType());

        resignHistoryType = new HistoryType();
        resignHistoryType.setId(RESIGN_TYPE_ID);
        resignHistoryType.setType(HistoryTypeEnum.RENUNCIA.getType());

        genericHistoryType = new HistoryType();
        genericHistoryType.setId(VALID_TYPE_ID);
        genericHistoryType.setType(VALID_TYPE_NAME);
    }

    @Test
    public void succesfullyFindHistoryTypeByName() throws NotFoundException {
        // ARRANGE
        when(historyTypeRepository.findByType(VALID_TYPE_NAME)).thenReturn(Optional.of(genericHistoryType));

        // ACT
        HistoryType result = historyTypeService.findHistoryTypeByName(VALID_TYPE_NAME);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(VALID_TYPE_ID, result.getId()),
            () -> assertEquals(VALID_TYPE_NAME, result.getType())
        );
    }

    @Test
    public void notFoundFindHistoryTypeByName() {
        // ARRANGE
        when(historyTypeRepository.findByType(VALID_TYPE_NAME)).thenReturn(Optional.empty());

        // ASSERT
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> historyTypeService.findHistoryTypeByName(VALID_TYPE_NAME),
            "Expected NotFoundException when HistoryType not found by name");
        assertEquals(NOT_FOUND_MESSAGE_NAME, exception.getMessage());
    }

    @Test
    public void succesfullyFindHistoryTypeById() throws NotFoundException {
        // ARRANGE
        when(historyTypeRepository.findById(VALID_TYPE_ID)).thenReturn(Optional.of(genericHistoryType));

        // ACT
        HistoryType result = historyTypeService.findHistoryTypeById(VALID_TYPE_ID);

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(VALID_TYPE_ID, result.getId()),
            () -> assertEquals(VALID_TYPE_NAME, result.getType())
        );
    }

    @Test
    public void notFoundFindHistoryTypeById() {
        // ARRANGE
        when(historyTypeRepository.findById(VALID_TYPE_ID)).thenReturn(Optional.empty());

        // ASSERT
        NotFoundException exception = assertThrows(NotFoundException.class,
            () -> historyTypeService.findHistoryTypeById(VALID_TYPE_ID));
        assertEquals(NOT_FOUND_MESSAGE_ID, exception.getMessage());
    }

    @Test
    public void succesfullyFindDeactivationTypes() {
        // ARRANGE
        when(historyTypeRepository.findByType(HistoryTypeEnum.DESPIDO.getType())).thenReturn(Optional.of(firingHistoryType));
        when(historyTypeRepository.findByType(HistoryTypeEnum.RENUNCIA.getType())).thenReturn(Optional.of(resignHistoryType));

        // ACT
        List<HistoryType> result = historyTypeService.findDeactivationHistoryTypes();

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(2, result.size()),
            () -> assertEquals(HistoryTypeEnum.DESPIDO.getType(), result.get(0).getType()),
            () -> assertEquals(HistoryTypeEnum.RENUNCIA.getType(), result.get(1).getType())
        );
    }

    @Test
    public void onlyFiringFindDeactivationTypes() {
        // ARRANGE
        when(historyTypeRepository.findByType(HistoryTypeEnum.DESPIDO.getType())).thenReturn(Optional.of(firingHistoryType));
        when(historyTypeRepository.findByType(HistoryTypeEnum.RENUNCIA.getType())).thenReturn(Optional.empty());

        // ACT
        List<HistoryType> result = historyTypeService.findDeactivationHistoryTypes();

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(1, result.size()),
            () -> assertEquals(HistoryTypeEnum.DESPIDO.getType(), result.get(0).getType())
        );
    }

    @Test
    public void notFoundFindDeactivationTypes() {
        // ARRANGE
        when(historyTypeRepository.findByType(HistoryTypeEnum.DESPIDO.getType())).thenReturn(Optional.empty());
        when(historyTypeRepository.findByType(HistoryTypeEnum.RENUNCIA.getType())).thenReturn(Optional.empty());

        // ACT
        List<HistoryType> result = historyTypeService.findDeactivationHistoryTypes();

        // ASSERT
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(0, result.size())
        );
    }
}
