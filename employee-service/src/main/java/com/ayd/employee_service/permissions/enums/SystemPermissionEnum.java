package com.ayd.employee_service.permissions.enums;

import com.ayd.employee_service.permissions.models.Permission;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SystemPermissionEnum {

        // PERMISOS DE EMPLEADOS
        CREATE_EMPLOYEE(new Permission("Crear empleado", "CREATE_EMPLOYEE")),
        EDIT_EMPLOYEE(new Permission("Editar empleado", "EDIT_EMPLOYEE")),
        DESACTIVATE_EMPLOYEE(new Permission("Desactivar empleado", "DESACTIVATE_EMPLOYEE")),
        RESACTIVATE_EMPLOYEE(new Permission("Reactivar empleado", "RESACTIVATE_EMPLOYEE")),
        UPDATE_EMPLOYEE_SALARY(new Permission("Actualiza el sueldo de un empleado", "UPDATE_EMPLOYEE_SALARY")),


   

        // PARA TIPOS DE EMPLEADO
        CREATE_EMPLOYEE_TYPE(new Permission("Crear tipo de empleado", "CREATE_EMPLOYEE_TYPE")),
        EDIT_EMPLOYEE_TYPE(new Permission("Editar tipo de empleado", "EDIT_EMPLOYEE_TYPE")),
        DELETE_EMPLOYEE_TYPE(new Permission("Eliminar tipo de empleado", "DELETE_EMPLOYEE_TYPE")),


        // para las vacaciones
        CHANGE_VACATION_DAYS(new Permission("Cambiar dias de vacaciones", "CHANGE_VACATION_DAYS")),
        GET_ALL_INVOICES(new Permission("Obtener a todos los empleados que pueden sacar finiquito", "GET_ALL_INVOICES")),

        // Permisos de productos
        CREATE_PRODUCT(new Permission("Crear producto", "CREATE_PRODUCT")),
        EDIT_PRODUCT(new Permission("Editar producto", "EDIT_PRODUCT")),
        DELETE_PRODUCT(new Permission("Eliminar producto", "DELETE_PRODUCT")),

        ;

        private final Permission permission;
}
