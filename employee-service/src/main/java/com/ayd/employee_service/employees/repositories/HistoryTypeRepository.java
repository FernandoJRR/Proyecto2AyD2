package com.ayd.employee_service.employees.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.employee_service.employees.models.HistoryType;

public interface HistoryTypeRepository extends JpaRepository<HistoryType, String>{
    /**
     * Busca si el nombre del tipo de historial existe en la base de datos por nombre
     *
     * @param name
     * @return
     */
    public Optional<HistoryType> findByType(String type);
}
