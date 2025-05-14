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

    // Permisos de productos
    CREATE_PRODUCT(new Permission("Crear producto", "CREATE_PRODUCT")),
    EDIT_PRODUCT(new Permission("Editar producto", "EDIT_PRODUCT")),
    DELETE_PRODUCT(new Permission("Eliminar producto", "DELETE_PRODUCT")),

    // Permisos de facturacion
    CREATE_INVOICE(new Permission("Crear factura", "CREATE_INVOICE")),

    // Permisos de reservas
    CREATE_RESERVATION(new Permission("Crear reserva presencial", "CREATE_PRESENTIAL_RESERVATION")),
    CANCEL_RESERVATION(new Permission("Cancelar reserva", "CANCEL_RESERVATION")),
    PAY_RESERVATION(new Permission("Pagar reserva", "PAY_RESERVATION")),
    DELETE_RESERVATION(new Permission("Eliminar reserva", "DELETE_RESERVATION")),

    // Permisos de horarios
    CREATE_SCHEDULE(new Permission("Crear horario", "CREATE_SCHEDULE")),
    EDIT_SCHEDULE(new Permission("Editar horario", "EDIT_SCHEDULE")),
    DELETE_SCHEDULE(new Permission("Eliminar horario", "DELETE_SCHEDULE")),

    // Permisos sobre cajas
    CREATE_CASH_REGISTER(new Permission("Crear caja", "CREATE_CASH_REGISTER")),
    EDIT_CASH_REGISTER(new Permission("Editar caja", "EDIT_CASH_REGISTER")),
    TOOGLE_CASH_REGISTER(new Permission("Activar o desactivar caja", "TOOGLE_CASH_REGISTER")),

    // Permisos de Entradas de productos
    CREATE_PRODUCT_ENTRY(new Permission("Crear entrada de producto", "CREATE_PRODUCT_ENTRY")),

    // Permisos de stock
    MODIFY_STOCK(new Permission("Modificar stock", "MODIFY_STOCK")),
    MODIFY_MINIMUN_STOCK(new Permission("Modificar stock minimo", "MODIFY_MINIMUN_STOCK")),

    // Permisos de proveedores
    CREATE_SUPPLIER(new Permission("Crear proveedor", "CREATE_SUPPLIER")),
    EDIT_SUPPLIER(new Permission("Editar proveedor", "EDIT_SUPPLIER")),
    TOOGLE_SUPPLIER(new Permission("Activar o desactivar proveedor", "TOOGLE_SUPPLIER")),

    // Permisos de bodegas
    CREATE_WAREHOUSE(new Permission("Crear bodega", "CREATE_WAREHOUSE")),
    EDIT_WAREHOUSE(new Permission("Editar bodega", "EDIT_WAREHOUSE")),
    TOOGLE_WAREHOUSE(new Permission("Activar o desactivar bodega", "TOOGLE_WAREHOUSE")),

    // permisos de paquetes
    CREATE_GOLF_PACKAGE(new Permission("Crear pacquetes", "CREATE_GOLF_PACKAGE")),
    EDIT_GOLF_PACKAGE(new Permission("Editar pacquetes", "EDIT_GOLF_PACKAGE")),

    // Permisos de Config
    UPDATE_CONFIG(new Permission("Actualizar las configuraciones", "UPDATE_CONFIG")),

    // Permisos de Juegos
    CREATE_GAME(new Permission("Crear juegos", "CREATE_GAME")),
    UPDATE_SCORE(new Permission("Actualizar punteos", "UPDATE_SCORE")),
    ;

    private final Permission permission;
}
