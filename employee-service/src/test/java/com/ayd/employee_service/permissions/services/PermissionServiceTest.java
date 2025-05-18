package com.ayd.employee_service.permissions.services;

import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.repositories.PermissionRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionService permissionService;

    private static final String PERMISSION_ID = "perm-001";
    private static final String PERMISSION_NAME = "GESTION_USUARIOS";

    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setId(PERMISSION_ID);
        permission.setName(PERMISSION_NAME);
    }

    /**
     * dado: que el nombre no está duplicado.
     * cuando: se llama a createPermission.
     * entonces: se guarda y retorna el permiso.
     */
    @Test
    public void createPermissionShouldSucceedWhenNameNotExists() throws DuplicatedEntryException {
        when(permissionRepository.existsByName(PERMISSION_NAME)).thenReturn(false);
        when(permissionRepository.save(permission)).thenReturn(permission);

        Permission result = permissionService.createPermission(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_NAME, result.getName());
        verify(permissionRepository).save(permission);
    }

    /**
     * dado: que el nombre ya existe.
     * cuando: se llama a createPermission.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void createPermissionShouldThrowWhenNameAlreadyExists() {
        when(permissionRepository.existsByName(PERMISSION_NAME)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> permissionService.createPermission(permission));
        verify(permissionRepository, never()).save(any());
    }

    /**
     * dado: que se pasa un permiso con ID válido.
     * cuando: se llama a findPermissionById.
     * entonces: se retorna el permiso.
     */
    @Test
    public void findPermissionByIdShouldReturnWhenExists() throws NotFoundException {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permission));

        Permission result = permissionService.findPermissionById(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_ID, result.getId());
        verify(permissionRepository).findById(PERMISSION_ID);
    }

    /**
     * dado: que el ID no existe.
     * cuando: se llama a findPermissionById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findPermissionByIdShouldThrowWhenNotFound() {
        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> permissionService.findPermissionById(permission));
    }

    /**
     * dado: que se pasa un permiso con nombre válido.
     * cuando: se llama a findPermissionByName.
     * entonces: se retorna el permiso.
     */
    @Test
    public void findPermissionByNameShouldReturnWhenExists() throws NotFoundException {
        when(permissionRepository.findByName(PERMISSION_NAME)).thenReturn(Optional.of(permission));

        Permission result = permissionService.findPermissionByName(permission);

        assertNotNull(result);
        assertEquals(PERMISSION_NAME, result.getName());
        verify(permissionRepository).findByName(PERMISSION_NAME);
    }

    /**
     * dado: que el nombre no existe.
     * cuando: se llama a findPermissionByName.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findPermissionByNameShouldThrowWhenNotFound() {
        when(permissionRepository.findByName(PERMISSION_NAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> permissionService.findPermissionByName(permission));
    }

    /**
     * dado: que todos los permisos tienen ID válido.
     * cuando: se llama a findAllById.
     * entonces: se retorna la lista correspondiente.
     */
    @Test
    public void findAllByIdShouldReturnAllWhenAllExist() throws NotFoundException {
        Permission another = new Permission();
        another.setId("perm-002");

        when(permissionRepository.findById(PERMISSION_ID)).thenReturn(Optional.of(permission));
        when(permissionRepository.findById("perm-002")).thenReturn(Optional.of(another));

        List<Permission> input = List.of(permission, another);
        List<Permission> result = permissionService.findAllById(input);

        assertEquals(2, result.size());
    }

    /**
     * dado: que uno de los permisos no existe.
     * cuando: se llama a findAllById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findAllByIdShouldThrowWhenOneNotFound() {
        Permission unknown = new Permission();
        unknown.setId("missing-id");

        when(permissionRepository.findById("missing-id")).thenReturn(Optional.empty());

        List<Permission> input = List.of(unknown);
        assertThrows(NotFoundException.class, () -> permissionService.findAllById(input));
    }

    /**
     * dado: que existen permisos en la base.
     * cuando: se llama a findAllPermissions.
     * entonces: se retorna la lista.
     */
    @Test
    public void findAllPermissionsShouldReturnAll() {
        when(permissionRepository.findAll()).thenReturn(List.of(permission));

        List<Permission> result = permissionService.findAllPemrissions();

        assertEquals(1, result.size());
        verify(permissionRepository).findAll();
    }
}
