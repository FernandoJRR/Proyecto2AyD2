package com.ayd.employee_service.employees.specifications;

import com.ayd.employee_service.employees.models.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {

    public static Specification<Employee> hasFirstName(String firstName) {
        return (root, query, cb) ->
                firstName == null ? null : cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<Employee> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null : cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<Employee> hasEmployeeTypeId(String employeeTypeId) {
        return (root, query, cb) ->
                employeeTypeId == null ? null : cb.equal(root.get("employeeType").get("id"), employeeTypeId);
    }

    public static Specification<Employee> isActive(Boolean active) {
        return (root, query, cb) -> {
            if (active == null) return null;
            if (active)
                return cb.isNull(root.get("desactivatedAt"));
            else
                return cb.isNotNull(root.get("desactivatedAt"));
        };
    }
}
