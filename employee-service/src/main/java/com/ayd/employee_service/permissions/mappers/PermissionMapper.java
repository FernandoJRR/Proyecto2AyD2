package com.ayd.employee_service.permissions.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.ayd.employee_service.permissions.dtos.PermissionResponseDTO;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.shared.dtos.IdRequestDTO;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     * Crea una lista de objetos Permission inicializando solamente sus ids.
     *
     * @param dto
     * @return
     */
    public List<Permission> fromIdsRequestDtoToPermissions(List<IdRequestDTO> dto);

    /**
     * Convierte una lista de entidades `Permission` a una lista de
     * PermissionResponseDTO.
     *
     * @param permissions Lista de permisos a convertir.
     * @return Lista de `PermissionResponseDTO` con los datos mapeados.
     */
    public List<PermissionResponseDTO> fromPermissionsToPermissionsReponseDtos(List<Permission> permissions);

}
