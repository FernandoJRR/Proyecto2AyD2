package com.ayd.employee_service.auth.login.service;

import com.ayd.employee_service.auth.jwt.ports.ForJwtGenerator;
import com.ayd.employee_service.auth.login.dtos.LoginResponseDTO;
import com.ayd.employee_service.auth.login.ports.ForUserLoader;
import com.ayd.employee_service.auth.login.service.LoginService;
import com.ayd.employee_service.employees.mappers.EmployeeMapper;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.permissions.mappers.PermissionMapper;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.shared.utils.PasswordEncoderUtil;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.ayd.sharedEmployeeService.dto.PermissionResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private ForJwtGenerator forJwtGenerator;
    @Mock
    private ForUsersPort forUsersPort;
    @Mock
    private ForUserLoader forUserLoader;
    @Mock
    private PasswordEncoderUtil passwordEncoder;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private LoginService loginService;

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "1234";
    private static final String HASHED_PASSWORD = "hashed1234";
    private static final String JWT = "jwt-token";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername(USERNAME);
        user.setPassword(HASHED_PASSWORD);
        user.setDesactivatedAt(null);
        Employee employee = new Employee();
        employee.setId("emp-1");

        // Mock de permisos y tipo de empleado
        Permission permission = new Permission();
        permission.setId("perm-1");
        permission.setName("READ");

        EmployeeType employeeType = new EmployeeType();
        employeeType.setId("type-1");
        employeeType.setName("ADMIN");
        employeeType.setPermissions(List.of(permission));

        employee.setEmployeeType(employeeType);
        user.setEmployee(employee);
    }

    /**
     * dado: credenciales válidas y usuario activo.
     * cuando: se llama a login.
     * entonces: se retorna correctamente el LoginResponseDTO con JWT.
     */
    @Test
    public void loginShouldSucceedWithValidCredentials() throws NotFoundException {
        when(forUsersPort.findUserByUsername(USERNAME)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);
        when(forUserLoader.loadUserPermissions(user)).thenReturn(List.of("READ"));
        when(forJwtGenerator.generateToken(user, List.of("READ"))).thenReturn(JWT);

        EmployeeResponseDTO employeeDTO = new EmployeeResponseDTO("emp-1", "Juan", "Pérez", null, null, null, null,
                null, null);
        List<PermissionResponseDTO> permissionDTOs = List.of(new PermissionResponseDTO("perm-1", "READ"));

        when(employeeMapper.fromEmployeeToResponse(user.getEmployee())).thenReturn(employeeDTO);
        when(permissionMapper.fromPermissionsToPermissionsReponseDtos(any())).thenReturn(permissionDTOs);

        LoginResponseDTO result = loginService.login(USERNAME, PASSWORD);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        assertEquals(JWT, result.getToken());
        assertEquals(employeeDTO, result.getEmployee());
        assertEquals(permissionDTOs, result.getPermissions());
    }

    /**
     * dado: contraseña incorrecta.
     * cuando: se llama a login.
     * entonces: se lanza BadCredentialsException.
     */
    @Test
    public void loginShouldThrowWhenPasswordInvalid() throws NotFoundException {
        when(forUsersPort.findUserByUsername(USERNAME)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> loginService.login(USERNAME, PASSWORD));
    }

    /**
     * dado: usuario desactivado.
     * cuando: se llama a login.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void loginShouldThrowWhenUserIsDeactivated() throws NotFoundException {
        user.setDesactivatedAt(LocalDate.now());
        when(forUsersPort.findUserByUsername(USERNAME)).thenReturn(user);
        when(passwordEncoder.matches(PASSWORD, HASHED_PASSWORD)).thenReturn(true);

        assertThrows(NotFoundException.class, () -> loginService.login(USERNAME, PASSWORD));
    }

    /**
     * dado: nombre de usuario no existe.
     * cuando: se llama a login.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void loginShouldThrowWhenUserNotFound() throws NotFoundException {
        when(forUsersPort.findUserByUsername(USERNAME)).thenThrow(new NotFoundException("no existe"));

        assertThrows(NotFoundException.class, () -> loginService.login(USERNAME, PASSWORD));
    }
}
