package com.ayd.employee_service.auth.login.ports;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ayd.employee_service.users.models.User;

public interface ForUserLoader {
    List<String> loadUserPermissions(User user) throws UsernameNotFoundException;
}
