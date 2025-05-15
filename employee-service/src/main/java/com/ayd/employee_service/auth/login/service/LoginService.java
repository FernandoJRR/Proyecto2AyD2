package com.ayd.employee_service.auth.login.service;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.ayd.employee_service.auth.jwt.ports.ForJwtGenerator;
import com.ayd.employee_service.auth.login.dtos.LoginResponseDTO;
import com.ayd.employee_service.auth.login.ports.ForLogin;
import com.ayd.employee_service.auth.login.ports.ForUserLoader;
import com.ayd.employee_service.employees.mappers.EmployeeMapper;
import com.ayd.employee_service.permissions.mappers.PermissionMapper;
import com.ayd.employee_service.shared.utils.PasswordEncoderUtil;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedEmployeeService.dto.EmployeeResponseDTO;
import com.ayd.sharedEmployeeService.dto.PermissionResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class LoginService implements ForLogin {

    private final ForJwtGenerator forJwtGenerator;
    private final ForUsersPort forUsersPort;
    private final ForUserLoader forUserLoader;

    private final PasswordEncoderUtil passwordEncoder;

    // mappers
    private final EmployeeMapper employeeMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public LoginResponseDTO login(String username, String password) throws NotFoundException, BadCredentialsException {

        // traer el usuario por nombre de usuario
        User user = forUsersPort.findUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("");
        }

        // verificar que el usuario no esté desactivado
        if (user.getDesactivatedAt() != null) {
            throw new NotFoundException("El usuario se encuentra desactivado.");
        }
        // si la autenticacion no fallo entonces cargamos los permisos del usuario
        List<String> authorities = forUserLoader.loadUserPermissions(user);
        // cagados los permisos entonces generamos el jwt
        String jwt = forJwtGenerator.generateToken(user, authorities);
        // construimos la respuesta
        EmployeeResponseDTO employeeResponseDTO = employeeMapper.fromEmployeeToResponse(user.getEmployee());

        List<PermissionResponseDTO> permissions = permissionMapper
                .fromPermissionsToPermissionsReponseDtos(user.getEmployee().getEmployeeType().getPermissions());
        return new LoginResponseDTO(user.getUsername(), employeeResponseDTO, jwt, permissions);

    }

}
