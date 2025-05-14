package com.ayd.employee_service.employees.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.employees.repositories.EmployeeTypeRepository;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.ports.ForPermissionsPort;
import com.ayd.employee_service.shared.enums.EmployeeTypeEnum;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;

@ExtendWith(MockitoExtension.class)
public class EmployeeTypeServiceTest {

    @Mock
    private EmployeeTypeRepository employeeTypeRepository;
    @Mock
    private ForPermissionsPort forPermissionsPort;

    @Mock
    private ForEmployeesPort forEmployeesPort;

    @InjectMocks
    private EmployeeTypeService employeeTypeService;

    private EmployeeType employeeType1;
    private EmployeeType employeeType2;

    private Permission permission1;
    private Permission permission2;
    private List<Permission> permissionsToAssing;

    private static final String EMPLOYEETYPE_ID_1 = "asdfg-asdfgasdf";
    private static final String EMPLOYEETYPE_NAME_1 = "Admin";

    private static final String EMPLOYEETYPE_ID_2 = "qwerty-qwerty";
    private static final String EMPLOYEETYPE_NAME_2 = "User";

    private static final String PERMISSION_ID_1 = "2345-asdfg";
    private static final String PERMISSION_NAME_1 = "Crear";
    private static final String PERMISSION_ID_2 = "sdfg-23453";
    private static final String PERMISSION_NAME_2 = "Editar";

    @BeforeEach
    public void setUp() {
        employeeType1 = new EmployeeType(EMPLOYEETYPE_ID_1, EMPLOYEETYPE_NAME_1);
        employeeType2 = new EmployeeType(EMPLOYEETYPE_ID_2, EMPLOYEETYPE_NAME_2);
        permission1 = new Permission(PERMISSION_ID_1, PERMISSION_NAME_1);
        permission2 = new Permission(PERMISSION_ID_2, PERMISSION_NAME_2);
        permissionsToAssing = List.of(permission1, permission2);
        // para los delete demos inicializar las asignaciones de los typos empleados
        employeeType1.setEmployees(List.of());
        employeeType2.setEmployees(List.of());
    }

    /**
     * dado: que el tipo de empleado no existe en la base de datos.
     * cuando: se intenta crear un nuevo tipo de empleado con permisos válidos.
     * entonces: el tipo de empleado se crea correctamente y se asignan los permisos
     * especificados.
     */
    @Test
    public void createEmployeeTypeShouldCreateEmployeeTypeSuccessfully()
            throws DuplicatedEntryException, NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.existsByName(anyString())).thenReturn(false);
        // con cualquier lista devolver el mok
        when(forPermissionsPort.findAllById(any())).thenReturn(permissionsToAssing);
        // con cualquier tipo de empleado devolver el mock
        when(employeeTypeRepository.save(any(EmployeeType.class))).thenReturn(employeeType1);
        // vamosa capturar el argumaento que se pasa al setPermissions
        ArgumentCaptor<EmployeeType> employeeTypeCaptor = ArgumentCaptor.forClass(EmployeeType.class);

        // ACT
        EmployeeType result = employeeTypeService.createEmployeeType(employeeType1, permissionsToAssing);

        verify(forPermissionsPort, times(1)).findAllById(permissionsToAssing);
        // verificamos que el save e haya hecho una vez y extraemos el parametro enviado
        verify(employeeTypeRepository, times(1)).save(employeeTypeCaptor.capture());
        EmployeeType capturedEmployeeType = employeeTypeCaptor.getValue();

        assertAll(
                // la respuesta no puede ser nula
                () -> assertNotNull(result),
                // verificamos que los permisos pasados al save seaan los mismos que pasamos al
                // metodo del service
                () -> assertEquals(permissionsToAssing, capturedEmployeeType.getPermissions()),
                // verificamos que el typo de empleado retornado sea igual al que mandamos (con
                // el id)
                () -> assertEquals(employeeType1, result));

    }

    /**
     * dado: que ya existe un tipo de empleado con el mismo nombre en la base de
     * datos.
     * cuando: se intenta crear un nuevo tipo de empleado con el mismo nombre.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guarda en
     * la base de datos.
     */
    @Test
    public void createEmployeeTypeShouldThrowDuplicatedEntryExceptionWhenNameAlreadyExists() {
        // ARRANGE
        when(employeeTypeRepository.existsByName(anyString())).thenReturn(true);

        // ACT y ASSERT
        assertThrows(DuplicatedEntryException.class,
                () -> employeeTypeService.createEmployeeType(employeeType1, permissionsToAssing));
        // verificar que el save nunca se hizo
        // podemos usar never() en vez de times(0) acabo de describirlo XD
        verify(employeeTypeRepository, never()).save(any(EmployeeType.class));
    }

    /**
     * dado: que los permisos especificados no existen en la base de datos.
     * cuando: se intenta crear un nuevo tipo de empleado con estos permisos.
     * entonces: se lanza una excepción NotFoundException y no se guarda en la
     * base de datos.
     */
    @Test
    public void createEmployeeTypeShouldThrowNotFoundExceptionWhenPermissionsNotFound() throws NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.existsByName(anyString())).thenReturn(false);
        when(forPermissionsPort.findAllById(any())).thenThrow(new NotFoundException(anyString()));

        // ACT y ASSERT
        assertThrows(NotFoundException.class,
                () -> employeeTypeService.createEmployeeType(employeeType1, permissionsToAssing));

        verify(employeeTypeRepository, never()).save(any(EmployeeType.class)); // Nunca debe llamarse save()
    }

    /**
     * dado: que el tipo de empleado existe en la base de datos.
     * cuando: se actualiza su nombre y lista de permisos con datos válidos.
     * entonces: el tipo de empleado se actualiza correctamente y se guardan los
     * cambios.
     */
    @Test
    public void updateEmployeeTypeShouldUpdateEmployeeTypeSuccessfully()
            throws DuplicatedEntryException, NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.existsByNameAndIdIsNot(anyString(), anyString())).thenReturn(false);
        when(forPermissionsPort.findAllById(any())).thenReturn(permissionsToAssing);
        when(employeeTypeRepository.save(any(EmployeeType.class))).thenReturn(employeeType2);

        // vamos a capturar los argumentos pasados a los set
        ArgumentCaptor<EmployeeType> employeeTypeCaptor = ArgumentCaptor.forClass(EmployeeType.class);

        // ACT
        EmployeeType result = employeeTypeService.updateEmployeeType(EMPLOYEETYPE_ID_1, employeeType2,
                permissionsToAssing);

        // ASSERT
        verify(forPermissionsPort, times(1)).findAllById(permissionsToAssing);

        // verifcamos que el save se hizo una vez y capturamos su argumetnoS
        verify(employeeTypeRepository, times(1)).save(employeeTypeCaptor.capture());

        EmployeeType capturedEmployeeType = employeeTypeCaptor.getValue();
        assertEquals(permissionsToAssing, capturedEmployeeType.getPermissions());
        assertEquals(employeeType2.getName(), capturedEmployeeType.getName());
        assertEquals(employeeType2, result);
    }

    /**
     * dado: que otro tipo de empleado ya tiene el mismo nombre.
     * cuando: se intenta actualizar un tipo de empleado con ese nombre.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guardan
     * cambios.
     */
    @Test
    public void updateEmployeeTypeShouldThrowDuplicatedEntryExceptionWhenNameAlreadyExists() {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.existsByNameAndIdIsNot(anyString(), anyString())).thenReturn(true);

        // ACT y ASSERT
        assertThrows(DuplicatedEntryException.class,
                () -> employeeTypeService.updateEmployeeType(EMPLOYEETYPE_ID_1, employeeType2,
                        permissionsToAssing));
        // verificar que el save nunca se hizo
        verify(employeeTypeRepository, never()).save(any(EmployeeType.class));
    }

    /**
     * dado: que los permisos a asignar no existen en la base de datos.
     * cuando: se intenta actualizar un tipo de empleado con estos permisos.
     * entonces: se lanza una excepción NotFoundException y no se guardan cambios.
     */
    @Test
    public void updateEmployeeTypeShouldThrowNotFoundExceptionWhenPermissionsNotFound() throws NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.existsByNameAndIdIsNot(anyString(), anyString())).thenReturn(false);
        when(forPermissionsPort.findAllById(any())).thenThrow(new NotFoundException(anyString()));

        // ACT Y ASSERT
        assertThrows(NotFoundException.class, () -> employeeTypeService.updateEmployeeType(EMPLOYEETYPE_ID_1,
                employeeType2, permissionsToAssing));
        // verificar que el save nunca se hizo
        verify(employeeTypeRepository, never()).save(any(EmployeeType.class));
    }

    /**
     * dado: que el tipo de empleado existe en la base de datos y no tiene empleados
     * asignados.
     * cuando: se elimina el tipo de empleado.
     * entonces: el tipo de empleado se elimina correctamente y ya no existe en la
     * base de datos.
     */
    @Test
    public void deleteEmployeeTypeByIdShouldDeleteEmployeeTypeSuccessfully() throws NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.findByName(anyString())).thenReturn(Optional.of(employeeType2));
        when(employeeTypeRepository.existsById(anyString())).thenReturn(false);

        // ACT
        boolean result = employeeTypeService.deleteEmployeeTypeById(EMPLOYEETYPE_ID_1);

        // ASSERT
        // aqui se valida que el motod de reaisnacion nunca se llamo porque los mocks no
        // estan asignadosa ningun usuario
        verify(forEmployeesPort, never()).reassignEmployeeType(employeeType1.getEmployees(), "");
        // validamos que el delete no se haya ejecutado ninguna vez
        verify(employeeTypeRepository, times(1)).delete(employeeType1);
        // ahora verificamos que la respuesta sea true
        assertTrue(result);
    }

    /**
     * dado: que el tipo de empleado no existe en la base de datos.
     * cuando: se intenta eliminar por su ID.
     * entonces: se lanza una excepción NotFoundException y no se realiza ninguna
     * eliminación.
     */
    @Test
    public void deleteEmployeeTypeByIdShouldThrowNotFoundExceptionWhenEmployeeTypeNotFoundById() {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        // ACT y ASSERT
        // al haverse devueto un ontional vacio entonces debe lanzarse la NotFound
        assertThrows(NotFoundException.class, () -> employeeTypeService.deleteEmployeeTypeById(EMPLOYEETYPE_ID_1));
        // el delete no se debio hacer nunca
        verify(employeeTypeRepository, never()).delete(any(EmployeeType.class));
    }

    /**
     * dado: que el tipo de empleado a eliminar es el tipo por defecto.
     * cuando: se intenta eliminar el tipo de empleado por defecto.
     * entonces: se lanza una excepción IllegalStateException y la operación no se
     * ejecuta.
     */
    @Test
    public void deleteEmployeeTypeByIdShouldThrowIllegalStateExceptionWhenDeletingDefaultEmployeeType() {

        // ARRANGE
        // cuadno se busque or id entonces devolveremos el default
        when(employeeTypeRepository.findById(anyString()))
                .thenReturn(Optional.of(EmployeeTypeEnum.DEFAULT.getEmployeeType()));

        // ACT y ASSERT
        // aqui al tratar de eliminar el default entonces la comparacion del if se
        // cunplira y debera lanzar la excecion
        assertThrows(IllegalStateException.class,
                () -> employeeTypeService.deleteEmployeeTypeById(EMPLOYEETYPE_ID_1));

        verify(employeeTypeRepository, never()).delete(any(EmployeeType.class));
    }

    /**
     * dado: que el tipo de empleado por defecto no se encuentra en la base de
     * datos.
     * cuando: se intenta reasignar empleados al tipo de empleado por defecto antes
     * de eliminar otro tipo.
     * entonces: se lanza una excepción NotFoundException y la eliminación no se
     * ejecuta.
     */
    @Test
    public void deleteEmployeeTypeByIdShouldThrowNotFoundExceptionWhenDefaultEmployeeTypeNotFound() {
        // ARRANGE
        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.findByName(anyString())).thenReturn(Optional.empty());

        // ACT y ASSERT
        // al buscarse el tupo por defecto se devolvera un optional vacio entones se
        // debe lanzar el NotFOund
        assertThrows(NotFoundException.class, () -> employeeTypeService.deleteEmployeeTypeById(EMPLOYEETYPE_ID_1));
        // no debio hacerse el delete
        verify(employeeTypeRepository, never()).delete(any(EmployeeType.class));
    }

    /**
     * dado: que la reasignación de empleados a otro tipo falla.
     * cuando: se intenta eliminar un tipo de empleado que tiene empleados
     * asignados.
     * entonces: se lanza una excepción NotFoundException y la eliminación no se
     * ejecuta.
     */
    @Test
    public void shouldThrowNotFoundExceptionWhenReassignFails() throws NotFoundException {
        // ARRANGE
        // llenamos la lista de las asignaciones para que entre al if
        employeeType1.setEmployees(List.of(new Employee()));

        when(employeeTypeRepository.findById(anyString())).thenReturn(Optional.of(employeeType1));
        when(employeeTypeRepository.findByName(anyString()))
                .thenReturn(Optional.of(employeeType2));
        when(forEmployeesPort.reassignEmployeeType(employeeType1.getEmployees(), employeeType2.getId()))
                .thenThrow(new NotFoundException(""));

        // ACT y ASSERT
        // hacerse la reasignacion entonces le lanzara la excepcion, debemos validarla
        assertThrows(NotFoundException.class, () -> employeeTypeService.deleteEmployeeTypeById(EMPLOYEETYPE_ID_1));
        // el delete nunca debe ejecutarse
        verify(employeeTypeRepository, never()).delete(any(EmployeeType.class));
    }

    /**
     * dado: que el tipo de empleado existe en la base de datos.
     * cuando: se consulta su existencia por ID.
     * entonces: el método devuelve true.
     */
    @Test
    public void existsEmployeeTypeByIdShouldReturnTrueWhenEmployeeTypeExistsById() throws NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.existsById(anyString())).thenReturn(true);
        // ACT
        boolean result = employeeTypeService.existsEmployeeTypeById(employeeType1.getId());
        // ASSERTS
        assertTrue(result);
    }

    /**
     * dado: que el tipo de empleado no existe en la base de datos.
     * cuando: se consulta su existencia por ID.
     * entonces: el método devuelve false.
     */
    @Test
    public void existsEmployeeTypeByIdShouldReturnFalseWhenEmployeeTypeDoesNotExistById() {
        // ARRANGE
        when(employeeTypeRepository.existsById(anyString())).thenReturn(false);
        // ACT
        boolean result = employeeTypeService.existsEmployeeTypeById(EMPLOYEETYPE_ID_1);
        // ASSERTS
        assertFalse(result);
    }

    /**
     * dado: que el tipo de empleado existe en la base de datos.
     * cuando: se consulta su existencia por nombre.
     * entonces: el método devuelve true.
     */
    @Test
    public void existsEmployeeTypeByNameShouldReturnTrueWhenEmployeeTypeExistsByName() throws NotFoundException {
        // ARRANGE
        when(employeeTypeRepository.existsByName(anyString())).thenReturn(true);
        // ACT
        boolean result = employeeTypeService.existsEmployeeTypeByName(EMPLOYEETYPE_ID_1);
        // ASSERTS
        assertTrue(result);
    }

    /**
     * dado: que el tipo de empleado no existe en la base de datos.
     * cuando: se consulta su existencia por nombre.
     * entonces: el método devuelve false.
     */
    @Test
    public void existsEmployeeTypeByNameShouldReturnFalseWhenEmployeeTypeDoesNotExistByName() {
        // ARRANGE
        when(employeeTypeRepository.existsByName(anyString())).thenReturn(false);
        // ACT
        boolean result = employeeTypeService.existsEmployeeTypeByName(EMPLOYEETYPE_ID_1);
        // ASSERTS
        assertFalse(result);
    }

    /**
     * dado: que existen tipos de empleados en la base de datos.
     * cuando: se consultan todos los tipos de empleados.
     * entonces: el método devuelve una lista con los tipos de empleados
     * registrados.
     */
    @Test
    public void findAllEmployeesTypesShouldReturnListOfEmployeeTypesWhenTheyExist() {
        // ARRANGE
        List<EmployeeType> mockEmployeeTypes = List.of(employeeType1, employeeType2);
        when(employeeTypeRepository.findAll()).thenReturn(mockEmployeeTypes);

        // ACT
        List<EmployeeType> result = employeeTypeService.findAllEmployeesTypes();

        // ASSERT
        // verifica que la lista tiene dos elementos
        assertEquals(2, result.size());
        // verifica que employeeType1 está en la lista
        assertTrue(result.contains(employeeType1));
        // verifica que employeeType2 está en la lista
        assertTrue(result.contains(employeeType2));
    }

    /**
     * dado: que no existen tipos de empleados en la base de datos.
     * cuando: se consultan todos los tipos de empleados.
     * entonces: el método devuelve una lista vacía.
     */
    @Test
    public void findAllEmployeesTypesShouldReturnEmptyListWhenNoEmployeeTypesExist() {
        // ARRANGE
        when(employeeTypeRepository.findAll()).thenReturn(List.of());

        // ACT
        List<EmployeeType> result = employeeTypeService.findAllEmployeesTypes();

        // ASSERT
        assertTrue(result.isEmpty()); // la lista debe estar vacía
    }

}
