/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ayd.employee_service.auth.login.utils;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.ayd.employee_service.auth.login.ports.ForUserLoader;
import com.ayd.employee_service.users.models.User;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class UserLoaderUtil implements ForUserLoader {

    /**
     * Carga los permisos asociados a un usuario a partir de su tipo de empleado.
     *
     * @param user El usuario cuyos permisos serán cargados.
     * @return Un conjunto de `GrantedAuthority` con los permisos del usuario.
     */
    @Override
    public List<String> loadUserPermissions(User user) throws UsernameNotFoundException {
        // verificamos que ninguno de los componentes necesarios venga nulo, de ser asi
        // entonces retornamos un vacio
        if (user == null || user.getEmployee() == null || user.getEmployee().getEmployeeType() == null) {
            return List.of();
        }

        return user.getEmployee().getEmployeeType().getPermissions().stream()
                .map(permission -> permission.getAction()).toList();
    }

}
