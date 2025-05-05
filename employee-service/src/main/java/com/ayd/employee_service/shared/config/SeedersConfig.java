package com.ayd.employee_service.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ayd.employee_service.employees.enums.HistoryTypeEnum;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.employees.repositories.EmployeeRepository;
import com.ayd.employee_service.employees.repositories.EmployeeTypeRepository;
import com.ayd.employee_service.employees.repositories.HistoryTypeRepository;
/*
import com.ayd.employee_service.parameters.enums.ParameterEnum;
import com.ayd.employee_service.parameters.models.Parameter;
import com.ayd.employee_service.parameters.repositories.ParameterRepository;
*/
import com.ayd.employee_service.permissions.enums.SystemPermissionEnum;
import com.ayd.employee_service.permissions.models.Permission;
import com.ayd.employee_service.permissions.ports.ForPermissionsPort;
import com.ayd.employee_service.permissions.repositories.PermissionRepository;
import com.ayd.employee_service.shared.enums.EmployeeTypeEnum;
import com.ayd.employee_service.shared.utils.PasswordEncoderUtil;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Profile("dev || prod || local")
@RequiredArgsConstructor
@Component
public class SeedersConfig implements CommandLineRunner {

	private final ForEmployeeTypePort forEmployeeTypePort;
	private final ForPermissionsPort forPermissionsPort;
	private final ForEmployeesPort forEmployeesPort;

	private final PermissionRepository permissionRepository;
	private final UserRepository userRepository;
	//private final ParameterRepository parameterRepository;
	private final EmployeeTypeRepository employeeTypeRepository;
	private final EmployeeRepository employeeRepository;
	private final HistoryTypeRepository historyTypeRepository;
	private final PasswordEncoderUtil passwordEncoderUtil;

	@Override
	@Transactional(rollbackOn = Exception.class)
	public void run(String... args) throws Exception {
		System.out.println("Ejecutnado el metodo de seeders.");
		if (permissionRepository.count() > 0) {
			return;
		}

		System.out.println("Creando los seeders.");
		// en este array guardaremos los pemrisos creados
		List<Permission> createdPermissions = new ArrayList<>();

		// en este array guardaremos los permisos de doctor
		List<Permission> doctorPermissions = new ArrayList<>();

		// en este array guardaremos los permisos de enfermero
		List<Permission> enfermeroPermissions = new ArrayList<>();

		// en este array guardaremos los permisos de farmaceutico
		List<Permission> farmaceuticoPermissions = new ArrayList<>();

		// cremos los permisos
		for (SystemPermissionEnum permissionEnum : SystemPermissionEnum.values()) {
			Permission createdPermission = forPermissionsPort
					.createPermission(permissionEnum.getPermission());
			createdPermissions.add(createdPermission);
			if (validPermissionDoctor(permissionEnum)) {
				doctorPermissions.add(createdPermission);
			}
			if (validPermissionEnfermero(permissionEnum)) {
				enfermeroPermissions.add(createdPermission);
			}
			if (validPermissionFarmaceutico(permissionEnum)) {
				farmaceuticoPermissions.add(createdPermission);
			}
		}

		// mandamos a crear el tipo de empleado admin
		EmployeeType adminEmployeeType = forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.ADMIN.getEmployeeType(),
				createdPermissions);

		// creamos el sin asignar sin permisos
		forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.DEFAULT.getEmployeeType(),
				List.of());

		// Creamos el tipo de empleado doctor
		forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.DOCTOR.getEmployeeType(),
				doctorPermissions);

		// Creamos el tipo de empleado enfermero
		forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.ENFERMERO.getEmployeeType(),
				enfermeroPermissions);

		// Creamos el tipo de empleado farmaceutico
		forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.FARMACEUTICO.getEmployeeType(),
				farmaceuticoPermissions);

		// creamos el nuevo empleado adminstrador
		// creamos un nuevo empleado
		Employee newEmployee = new Employee("3349991110901","Luis", "Monterroso", new BigDecimal(2000),
				new BigDecimal(10), new BigDecimal(10), null,
				adminEmployeeType, null);
		// creamos el usuario admin
		User userAdmin = new User("admin", "admin");

		System.out.println("Ejecutando seeders.");
		// creamos el tipo de usuario
		EmployeeType newEmployeeType = new EmployeeType("USER");
		employeeTypeRepository.save(newEmployeeType);

		// se crean los tipos de historial
		HistoryType historyTypeContratacion = new HistoryType(HistoryTypeEnum.CONTRATACION.getType());
		HistoryType historyTypeDespido = new HistoryType(HistoryTypeEnum.DESPIDO.getType());
		HistoryType historyTypeRenuncia = new HistoryType(HistoryTypeEnum.RENUNCIA.getType());
		HistoryType historyTypeRecontratacion = new HistoryType(HistoryTypeEnum.RECONTRATACION.getType());
		HistoryType historyTypeAumentoSalarial = new HistoryType(HistoryTypeEnum.AUMENTO_SALARIAL.getType());
		HistoryType historyTypeDisminucionSalarial = new HistoryType(HistoryTypeEnum.DISMINUCION_SALARIAL.getType());

		// creamos el usuario admin
		User newUser = new User("admin", passwordEncoderUtil.encode("admin"));

		historyTypeRepository.save(historyTypeContratacion);
		historyTypeRepository.save(historyTypeDespido);
		historyTypeRepository.save(historyTypeRenuncia);
		historyTypeRepository.save(historyTypeRecontratacion);
		historyTypeRepository.save(historyTypeAumentoSalarial);
		historyTypeRepository.save(historyTypeDisminucionSalarial);

        /*
        for (ParameterEnum parameterEnum : ParameterEnum.values()) {
            Parameter currentParameter = new Parameter(parameterEnum.getKey(), parameterEnum.getDefaultValue(), parameterEnum.getName());
            parameterRepository.save(currentParameter);
        }
        */

		EmployeeHistory employeeHistoryAdmin = new EmployeeHistory("Creacion");
		employeeHistoryAdmin.setHistoryDate(LocalDate.now());
		employeeHistoryAdmin.setHistoryType(historyTypeContratacion);

		forEmployeesPort.createEmployee(newEmployee, adminEmployeeType, userAdmin, employeeHistoryAdmin);
	}

	private boolean validPermissionDoctor(SystemPermissionEnum permissionEnum) {
		return permissionEnum.name() == SystemPermissionEnum.CREATE_PATIENT.name() ||
				permissionEnum.name() == SystemPermissionEnum.CREATE_SALE_MEDICINE_CONSULT.name() ||
				permissionEnum.name() == SystemPermissionEnum.CREATE_CONSULT.name() ||
				permissionEnum.name() == SystemPermissionEnum.EDIT_CONSULT.name() ||
				permissionEnum.name() == SystemPermissionEnum.PAGO_CONSULT.name() ||
				permissionEnum.name() == SystemPermissionEnum.CREATE_SURGERY.name() ||
				permissionEnum.name() == SystemPermissionEnum.EDIT_SURGERY.name() ||
				permissionEnum.name() == SystemPermissionEnum.DELETE_SURGERY.name();
	}

	private boolean validPermissionEnfermero(SystemPermissionEnum permissionEnum) {
		return permissionEnum.name() == SystemPermissionEnum.CREATE_PATIENT.name() ||
				permissionEnum.name() == SystemPermissionEnum.CREATE_SALE_MEDICINE_CONSULT.name();
	}

	private boolean validPermissionFarmaceutico(SystemPermissionEnum permissionEnum) {
		return permissionEnum.name() == SystemPermissionEnum.CREATE_SALE_MEDICINE_FARMACIA.name() ||
				permissionEnum.name() == SystemPermissionEnum.EDIT_MEDICINE.name() ||
				permissionEnum.name() == SystemPermissionEnum.CREATE_MEDICINE.name();
	}

}
