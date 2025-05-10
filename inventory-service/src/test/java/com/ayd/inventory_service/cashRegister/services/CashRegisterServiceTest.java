package com.ayd.inventory_service.cashRegister.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.ayd.inventory_service.cashRegister.dtos.CreateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.SpecificationCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.dtos.UpdateCashRegisterRequestDTO;
import com.ayd.inventory_service.cashRegister.models.CashRegister;
import com.ayd.inventory_service.cashRegister.repositories.CashRegisterRepository;
import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

@ExtendWith(MockitoExtension.class)
public class CashRegisterServiceTest {
    @Mock
    private CashRegisterRepository cashRegisterRepository;

    @Mock
    private ForWarehousePort forWarehousePort;

    @InjectMocks
    private CashRegisterService cashRegisterService;

    private static final String CASH_REGISTER_ID = "cash-001";
    private static final String CASH_REGISTER_CODE = "CAJA001";
    private static final String EMPLOYEE_ID = "emp-001";
    private static final String WAREHOUSE_ID = "wh-001";
    private static final boolean CASH_REGISTER_ACTIVE = true;

    private static final String UPDATED_CODE = "CAJA002";
    private static final String UPDATED_EMPLOYEE_ID = "emp-002";
    private static final String UPDATED_WAREHOUSE_ID = "wh-002";
    private static final boolean UPDATED_ACTIVE = false;

    private CreateCashRegisterRequestDTO createRequestDTO;
    private UpdateCashRegisterRequestDTO updateRequestDTO;
    private Warehouse warehouse;
    private CashRegister cashRegister;

    @BeforeEach
    void setUp() {
        createRequestDTO = new CreateCashRegisterRequestDTO(CASH_REGISTER_CODE, CASH_REGISTER_ACTIVE, WAREHOUSE_ID,
                EMPLOYEE_ID);
        updateRequestDTO = new UpdateCashRegisterRequestDTO(UPDATED_CODE, UPDATED_ACTIVE, UPDATED_WAREHOUSE_ID,
                UPDATED_EMPLOYEE_ID);
        warehouse = new Warehouse();
        warehouse.setId(WAREHOUSE_ID);
        cashRegister = new CashRegister();
        cashRegister.setId(CASH_REGISTER_ID);
        cashRegister.setCode(CASH_REGISTER_CODE);
        cashRegister.setEmployeeId(EMPLOYEE_ID);
        cashRegister.setActive(CASH_REGISTER_ACTIVE);
        cashRegister.setWarehouse(warehouse);
    }

    /**
     * dado: que existen registros de caja en la base de datos.
     * cuando: se llama al método findAll.
     * entonces: se retorna la lista completa de registros de caja.
     */
    @Test
    public void findAllShouldReturnAllCashRegisters() {
        // Arrange
        List<CashRegister> expectedCashRegisters = List.of(cashRegister);
        when(cashRegisterRepository.findAll()).thenReturn(expectedCashRegisters);

        // Act
        List<CashRegister> result = cashRegisterService.findAll();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(CASH_REGISTER_CODE, result.get(0).getCode()));

        verify(cashRegisterRepository).findAll();
    }

    /**
     * dado: que se recibe un objeto SpecificationCashRegisterRequestDTO nulo.
     * cuando: se llama al método findAllBySpecification.
     * entonces: se retorna la lista completa de registros de caja.
     */
    @Test
    public void findAllBySpecificationShouldReturnAllWhenSpecIsNull() {
        // Arrange
        when(cashRegisterRepository.findAll()).thenReturn(List.of(cashRegister));

        // Act
        List<CashRegister> result = cashRegisterService.findAllBySpecification(null);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(CASH_REGISTER_CODE, result.get(0).getCode()));

        verify(cashRegisterRepository).findAll();
    }

    /**
     * dado: que se recibe un objeto SpecificationCashRegisterRequestDTO con
     * filtros.
     * cuando: se llama al método findAllBySpecification.
     * entonces: se aplica la Specification y se retorna la lista filtrada.
     */
    @Test
    public void findAllBySpecificationShouldReturnFilteredResults() {
        // Arrange
        SpecificationCashRegisterRequestDTO dto = new SpecificationCashRegisterRequestDTO(
                CASH_REGISTER_ID,
                CASH_REGISTER_CODE,
                CASH_REGISTER_ACTIVE,
                EMPLOYEE_ID,
                WAREHOUSE_ID);
        when(cashRegisterRepository.findAll(any(Specification.class))).thenReturn(List.of(cashRegister));

        // Act
        List<CashRegister> result = cashRegisterService.findAllBySpecification(dto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(CASH_REGISTER_CODE, result.get(0).getCode()));

        verify(cashRegisterRepository).findAll(any(Specification.class));
    }

    /**
     * dado: que existe un registro de caja con el ID proporcionado.
     * cuando: se llama al método findById.
     * entonces: se retorna el registro encontrado correctamente.
     */
    @Test
    public void findByIdShouldReturnCashRegisterWhenExists() throws NotFoundException {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.of(cashRegister));

        // Act
        CashRegister result = cashRegisterService.findById(CASH_REGISTER_ID);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(CASH_REGISTER_ID, result.getId()),
                () -> assertEquals(CASH_REGISTER_CODE, result.getCode()));

        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
    }

    /**
     * dado: que no existe un registro de caja con el ID proporcionado.
     * cuando: se llama al método findById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findByIdShouldThrowNotFoundExceptionWhenNotExists() {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cashRegisterService.findById(CASH_REGISTER_ID));

        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
    }

    /**
     * dado: que existe un registro de caja con el código proporcionado.
     * cuando: se llama al método findByCode.
     * entonces: se retorna el registro encontrado correctamente.
     */
    @Test
    public void findByCodeShouldReturnCashRegisterWhenExists() throws NotFoundException {
        // Arrange
        when(cashRegisterRepository.findByCode(CASH_REGISTER_CODE)).thenReturn(Optional.of(cashRegister));

        // Act
        CashRegister result = cashRegisterService.findByCode(CASH_REGISTER_CODE);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(CASH_REGISTER_ID, result.getId()),
                () -> assertEquals(CASH_REGISTER_CODE, result.getCode()));

        verify(cashRegisterRepository).findByCode(CASH_REGISTER_CODE);
    }

    /**
     * dado: que no existe un registro de caja con el código proporcionado.
     * cuando: se llama al método findByCode.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findByCodeShouldThrowNotFoundExceptionWhenNotExists() {
        // Arrange
        when(cashRegisterRepository.findByCode(CASH_REGISTER_CODE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cashRegisterService.findByCode(CASH_REGISTER_CODE));

        verify(cashRegisterRepository).findByCode(CASH_REGISTER_CODE);
    }

    /**
     * dado: una solicitud válida sin código duplicado ni empleado asignado.
     * cuando: se llama a save().
     * entonces: se guarda correctamente el registro de caja.
     */
    @Test
    public void save_shouldCreateCashRegister_whenNoDuplicatesAndNoEmployee()
            throws NotFoundException, DuplicatedEntryException {
        // Arrange
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(cashRegisterRepository.existsByCode(CASH_REGISTER_CODE)).thenReturn(false);
        when(cashRegisterRepository.save(any(CashRegister.class))).thenReturn(cashRegister);

        // Act
        CashRegister result = cashRegisterService.save(createRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(CASH_REGISTER_CODE, result.getCode());
        verify(cashRegisterRepository).save(any(CashRegister.class));
    }

    /**
     * dado: una solicitud válida con un empleado asignado no duplicado.
     * cuando: se llama a save().
     * entonces: se guarda correctamente el registro de caja.
     */
    @Test
    public void save_shouldCreateCashRegister_whenNoDuplicatesAndEmployeeSet()
            throws NotFoundException, DuplicatedEntryException {
        // Arrange
        CreateCashRegisterRequestDTO dtoWithEmployee = new CreateCashRegisterRequestDTO(
                CASH_REGISTER_CODE, true, WAREHOUSE_ID, EMPLOYEE_ID);

        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(cashRegisterRepository.existsByCode(CASH_REGISTER_CODE)).thenReturn(false);
        when(cashRegisterRepository.existsByEmployeeId(EMPLOYEE_ID)).thenReturn(false);
        when(cashRegisterRepository.save(any(CashRegister.class))).thenReturn(cashRegister);

        // Act
        CashRegister result = cashRegisterService.save(dtoWithEmployee);

        // Assert
        assertNotNull(result);
        verify(cashRegisterRepository).save(any(CashRegister.class));
    }

    /**
     * dado: un código ya existente.
     * cuando: se llama a save().
     * entonces: se lanza DuplicatedEntryException.
     * 
     * @throws NotFoundException
     */
    @Test
    public void save_shouldThrowDuplicatedEntryException_whenCodeExists() throws NotFoundException {
        // Arrange
        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(cashRegisterRepository.existsByCode(CASH_REGISTER_CODE)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> cashRegisterService.save(createRequestDTO));
        verify(cashRegisterRepository, never()).save(any());
    }

    /**
     * dado: un empleado ya asignado a otra caja.
     * cuando: se llama a save().
     * entonces: se lanza DuplicatedEntryException.
     * 
     * @throws NotFoundException
     */
    @Test
    public void save_shouldThrowDuplicatedEntryException_whenEmployeeExists() throws NotFoundException {
        // Arrange
        CreateCashRegisterRequestDTO dtoWithEmployee = new CreateCashRegisterRequestDTO(
                CASH_REGISTER_CODE, true, WAREHOUSE_ID, EMPLOYEE_ID);

        when(forWarehousePort.getWarehouse(WAREHOUSE_ID)).thenReturn(warehouse);
        when(cashRegisterRepository.existsByCode(CASH_REGISTER_CODE)).thenReturn(false);
        when(cashRegisterRepository.existsByEmployeeId(EMPLOYEE_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> cashRegisterService.save(dtoWithEmployee));
        verify(cashRegisterRepository, never()).save(any());
    }

    /**
     * dado: un ID válido, sin código ni empleado duplicados.
     * cuando: se llama a update().
     * entonces: se actualiza correctamente el registro de caja.
     */
    @Test
    public void update_shouldUpdateSuccessfully_whenNoDuplicates() throws NotFoundException, DuplicatedEntryException {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.of(cashRegister));
        when(cashRegisterRepository.existsByCodeAndIdNot(UPDATED_CODE, CASH_REGISTER_ID)).thenReturn(false);
        when(cashRegisterRepository.existsByEmployeeIdAndIdNot(UPDATED_EMPLOYEE_ID, CASH_REGISTER_ID))
                .thenReturn(false);
        when(forWarehousePort.getWarehouse(UPDATED_WAREHOUSE_ID)).thenReturn(warehouse);

        // Act
        CashRegister result = cashRegisterService.update(CASH_REGISTER_ID, updateRequestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(UPDATED_CODE, result.getCode());
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
        verify(forWarehousePort).getWarehouse(UPDATED_WAREHOUSE_ID);
    }

    /**
     * dado: un ID inexistente.
     * cuando: se llama a update().
     * entonces: se lanza NotFoundException.
     * 
     * @throws NotFoundException
     */
    @Test
    public void update_shouldThrowNotFoundException_whenCashRegisterNotFound() throws NotFoundException {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cashRegisterService.update(CASH_REGISTER_ID, updateRequestDTO));
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
        verify(forWarehousePort, never()).getWarehouse(any());
    }

    /**
     * dado: un código duplicado en otro registro.
     * cuando: se llama a update().
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void update_shouldThrowDuplicatedEntryException_whenCodeAlreadyExists() {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.of(cashRegister));
        when(cashRegisterRepository.existsByCodeAndIdNot(UPDATED_CODE, CASH_REGISTER_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> cashRegisterService.update(CASH_REGISTER_ID, updateRequestDTO));
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
    }

    /**
     * dado: un empleado ya asignado a otra caja.
     * cuando: se llama a update().
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void update_shouldThrowDuplicatedEntryException_whenEmployeeAlreadyAssigned() {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.of(cashRegister));
        when(cashRegisterRepository.existsByCodeAndIdNot(UPDATED_CODE, CASH_REGISTER_ID)).thenReturn(false);
        when(cashRegisterRepository.existsByEmployeeIdAndIdNot(UPDATED_EMPLOYEE_ID, CASH_REGISTER_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> cashRegisterService.update(CASH_REGISTER_ID, updateRequestDTO));
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
    }

    /**
     * dado: un ID válido.
     * cuando: se llama a toggleActive().
     * entonces: se invierte el estado `active` y se guarda el cambio.
     */
    @Test
    public void toggleActive_shouldToggleStatusAndSave_whenCashRegisterExists() throws NotFoundException {
        // Arrange
        cashRegister.setActive(true);
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.of(cashRegister));
        when(cashRegisterRepository.save(any(CashRegister.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CashRegister result = cashRegisterService.toggleActive(CASH_REGISTER_ID);

        // Assert
        assertNotNull(result);
        assertFalse(result.isActive()); // Se invierte de true a false
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
        verify(cashRegisterRepository).save(cashRegister);
    }

    /**
     * dado: un ID inexistente.
     * cuando: se llama a toggleActive().
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void toggleActive_shouldThrowNotFoundException_whenCashRegisterDoesNotExist() {
        // Arrange
        when(cashRegisterRepository.findById(CASH_REGISTER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cashRegisterService.toggleActive(CASH_REGISTER_ID));
        verify(cashRegisterRepository).findById(CASH_REGISTER_ID);
        verify(cashRegisterRepository, never()).save(any());
    }

    /**
     * dado: un employeeId existente.
     * cuando: se llama a findByEmployeeId().
     * entonces: se retorna el CashRegister correspondiente.
     */
    @Test
    public void findByEmployeeId_shouldReturnCashRegister_whenExists() throws NotFoundException {
        // Arrange
        when(cashRegisterRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(Optional.of(cashRegister));

        // Act
        CashRegister result = cashRegisterService.findByEmployeeId(EMPLOYEE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(EMPLOYEE_ID, result.getEmployeeId());
        verify(cashRegisterRepository).findByEmployeeId(EMPLOYEE_ID);
    }

    /**
     * dado: un employeeId inexistente.
     * cuando: se llama a findByEmployeeId().
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findByEmployeeId_shouldThrowNotFoundException_whenNotFound() {
        // Arrange
        when(cashRegisterRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> cashRegisterService.findByEmployeeId(EMPLOYEE_ID));
        verify(cashRegisterRepository).findByEmployeeId(EMPLOYEE_ID);
    }

}
