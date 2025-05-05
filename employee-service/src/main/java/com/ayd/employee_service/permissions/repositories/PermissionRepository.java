package com.ayd.employee_service.permissions.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.employee_service.permissions.models.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    public Optional<Permission> findByName(String name);

    public boolean existsByName(String name);
}
