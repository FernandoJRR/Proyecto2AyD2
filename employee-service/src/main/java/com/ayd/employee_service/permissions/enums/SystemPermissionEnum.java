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

        // PERMISOS DE PACIENTE
        CREATE_PATIENT(new Permission("Crear paciente", "CREATE_PATIENT")),

        // PERMISOS PARA MEDICOS
        CREATE_MEDICINE(new Permission("Crear medicamento", "CREATE_MEDICINE")),
        EDIT_MEDICINE(new Permission("Editar medicamento", "EDIT_MEDICINE")),
        // DELETE_MEDICINE(new Permission("Eliminar medicamento", "DELETE_MEDICINE")),

        // PERMISOS PARA CONSULTAS
        CREATE_CONSULT(new Permission("Crear consulta", "CREATE_CONSULT")),
        EDIT_CONSULT(new Permission("Editar consulta", "EDIT_CONSULT")),
        DELETE_CONSULT(new Permission("Eliminar consulta", "DELETE_CONSULT")),
        PAGO_CONSULT(new Permission("Pagar consulta", "PAGO_CONSULT")),

        //PERMISOS PARA CIRUGIAS
        CREATE_TYPE_SURGERY(new Permission("Crear tipo de cirugia", "CREATE_TYPE_SURGERY")),
        EDIT_TYPE_SURGERY(new Permission("Editar tipo de cirugia", "EDIT_TYPE_SURGERY")),
        CREATE_SURGERY(new Permission("Crear cirugia", "CREATE_SURGERY")),
        EDIT_SURGERY(new Permission("Editar cirugia", "EDIT_SURGERY")),
        DELETE_SURGERY(new Permission("Eliminar cirugia", "DELETE_SURGERY")),

        // PERMISOS PARA SALAS
        CREATE_SALE_MEDICINE_FARMACIA(
                        new Permission("Registrar venta de medicamento en farmacia",
                        "CREATE_SALE_MEDICINE_FARMACIA")),
        CREATE_SALE_MEDICINE_CONSULT(
                        new Permission("Registrar venta de medicamento en consultorio",
                                        "CREATE_SALE_MEDICINE_CONSULT")),

        // PARA TIPOS DE EMPLEADO

        CREATE_EMPLOYEE_TYPE(new Permission("Crear tipo de empleado", "CREATE_EMPLOYEE_TYPE")),
        EDIT_EMPLOYEE_TYPE(new Permission("Editar tipo de empleado", "EDIT_EMPLOYEE_TYPE")),
        DELETE_EMPLOYEE_TYPE(new Permission("Eliminar tipo de empleado", "DELETE_EMPLOYEE_TYPE")),

        // para las habitaciones
        CREATE_ROOM(new Permission("Crear habitacion", "CREATE_ROOM")),
        EDIT_ROOM(new Permission("Editar habitacion", "EDIT_ROOM")),
        TOGGLE_ROOM_AVAILABILITY(
                        new Permission("Alternar disponibilidad de la habitación", "TOGGLE_ROOM_AVAILABILITY")),

        // para las vacaciones
        CHANGE_VACATION_DAYS(new Permission("Cambiar dias de vacaciones", "CHANGE_VACATION_DAYS")),
        GET_ALL_INVOICES(new Permission("Obtener a todos los empleados que pueden sacar finiquito", "GET_ALL_INVOICES")),

        ;

        private final Permission permission;
}
