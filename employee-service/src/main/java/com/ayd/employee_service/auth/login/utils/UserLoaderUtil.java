/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ayd.employee_service.auth.login.utils;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ayd.employee_service.auth.login.ports.ForUserLoader;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;

import jakarta.transaction.Transactional;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class UserLoaderUtil implements ForUserLoader {

    private final ForUsersPort forUsersPort;

    public UserLoaderUtil(@Lazy ForUsersPort forUsersPort) {
        this.forUsersPort = forUsersPort;
    }

    /**
     * Metodo usado para que Spring pueda cargar el usuario en el contexto de
     * seguridad.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // traer el usuario por nombre de usuario
            User user = forUsersPort.findUserByUsername(username);

            // cargamos sus roles y permisos
            Set<GrantedAuthority> permissions = loadUserPermissions(user);

            return new org.springframework.security.core.userdetails.User(username,
                    user.getPassword(),
                    permissions);
        } catch (NotFoundException e) { // si hubo not found entonces devolvemos el erro especifico para el usuario
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    /**
     * Carga los permisos asociados a un usuario a partir de su tipo de empleado.
     *
     * @param user El usuario cuyos permisos serán cargados.
     * @return Un conjunto de `GrantedAuthority` con los permisos del usuario.
     */
    public Set<GrantedAuthority> loadUserPermissions(User user) {
        // verificamos que ninguno de los componentes necesarios venga nulo, de ser asi
        // entonces retornamos un vacio
        if (user == null || user.getEmployee() == null || user.getEmployee().getEmployeeType() == null) {
            return Set.of();
        }

        return user.getEmployee().getEmployeeType().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getAction()))
                .collect(Collectors.toSet());
    }
}
