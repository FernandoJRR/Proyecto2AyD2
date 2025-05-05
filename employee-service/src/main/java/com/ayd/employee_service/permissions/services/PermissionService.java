package com.ayd.employee_service.permissions.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.ports.ForPermissionsPort;
import com.ayd.employee_service.permissions.repositories.PermissionRepository;
import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class PermissionService implements ForPermissionsPort {

    private final PermissionRepository permissionRepository;

    @Override
    public Permission createPermission(Permission permissionToCreate) throws DuplicatedEntryException {
        if (permissionRepository.existsByName(permissionToCreate.getName())) {
            throw new DuplicatedEntryException("Ya existe un permiso con el mismo nombre.");
        }

        return permissionRepository.save(permissionToCreate);
    }

    @Override
    public List<Permission> findAllById(List<Permission> permissions) throws NotFoundException {

        List<Permission> foundPermissions = new ArrayList<>();
        for (Permission permission : permissions) {
            // si se lanza excepcion entonces la propagamos
            Permission findedPermission = findPermissionById(permission);
            foundPermissions.add(findedPermission);
        }

        // si el for termina si nproblema entonces los ids de los permisos existen y se
        // pueden utilizar
        return foundPermissions;
    }

    @Override
    public Permission findPermissionByName(Permission permission) throws NotFoundException {
        String errorMessage = String.format(
                "El permiso %s no existe en el sistema.",
                permission.getName());
        return permissionRepository.findByName(permission.getName()).orElseThrow(
                () -> new NotFoundException(errorMessage));
    }

    @Override
    public Permission findPermissionById(Permission permission) throws NotFoundException {
        String errorMessage = String.format(
                "El permiso con id %s no existe en el sistema.",
                permission.getId());
        return permissionRepository.findById(permission.getId()).orElseThrow(
                () -> new NotFoundException(errorMessage));
    }

    @Override
    public List<Permission> findAllPemrissions() {
        return permissionRepository.findAll();
    }
}
