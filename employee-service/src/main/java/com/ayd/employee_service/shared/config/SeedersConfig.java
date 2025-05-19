package com.ayd.employee_service.shared.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.ayd.employee_service.employees.enums.HistoryTypeEnum;
import com.ayd.employee_service.employees.enums.PaymentTypeEnum;
import com.ayd.employee_service.employees.models.Employee;
import com.ayd.employee_service.employees.models.EmployeeHistory;
import com.ayd.employee_service.employees.models.EmployeeType;
import com.ayd.employee_service.employees.models.HistoryType;
import com.ayd.employee_service.employees.models.PaymentType;
import com.ayd.employee_service.employees.ports.ForEmployeeTypePort;
import com.ayd.employee_service.employees.ports.ForEmployeesPort;
import com.ayd.employee_service.employees.repositories.EmployeeRepository;
import com.ayd.employee_service.employees.repositories.EmployeeTypeRepository;
import com.ayd.employee_service.employees.repositories.HistoryTypeRepository;
import com.ayd.employee_service.employees.repositories.PaymentTypeRepository;
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
	private final PaymentTypeRepository paymentTypeRepository;
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


		// cremos los permisos
		for (SystemPermissionEnum permissionEnum : SystemPermissionEnum.values()) {
			Permission createdPermission = forPermissionsPort
					.createPermission(permissionEnum.getPermission());
			createdPermissions.add(createdPermission);
		}

		for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            PaymentType createdType = new PaymentType();
            createdType.setType(paymentTypeEnum.getType());
			paymentTypeRepository.save(createdType);
		}

		// mandamos a crear el tipo de empleado admin
		EmployeeType adminEmployeeType = forEmployeeTypePort.createEmployeeType(
				EmployeeTypeEnum.ADMIN.getEmployeeType(),
				createdPermissions);


		// creamos el nuevo empleado adminstrador
		// creamos un nuevo empleado
		Employee newEmployee = new Employee("3349991110901","Luis", "Monterroso", new BigDecimal(2000),
				new BigDecimal(10), new BigDecimal(10), null,
				adminEmployeeType, null);

		// creamos el usuario admin
		User userAdmin = new User("admin", "admin");



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

		historyTypeRepository.save(historyTypeContratacion);
		historyTypeRepository.save(historyTypeDespido);
		historyTypeRepository.save(historyTypeRenuncia);
		historyTypeRepository.save(historyTypeRecontratacion);
		historyTypeRepository.save(historyTypeAumentoSalarial);
		historyTypeRepository.save(historyTypeDisminucionSalarial);


		EmployeeHistory employeeHistoryAdmin = new EmployeeHistory("Creacion");
		employeeHistoryAdmin.setHistoryDate(LocalDate.now());
		employeeHistoryAdmin.setHistoryType(historyTypeContratacion);

		forEmployeesPort.createEmployee(newEmployee, adminEmployeeType, userAdmin, employeeHistoryAdmin);
	}





}
