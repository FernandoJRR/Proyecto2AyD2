package com.ayd.employee_service.auth.login.ports;

import org.springframework.security.authentication.BadCredentialsException;

import com.ayd.employee_service.auth.login.dtos.LoginResponseDTO;
import com.ayd.shared.exceptions.*;
public interface ForLogin {
    /**
     * Autentica al usuario con sus credenciales.
     *
     * @param username
     * @param password
     * @return
     * @throws NotFoundException
     * @throws BadCredentialsException
     */
    public LoginResponseDTO login(String username, String password) throws NotFoundException, BadCredentialsException;
}
