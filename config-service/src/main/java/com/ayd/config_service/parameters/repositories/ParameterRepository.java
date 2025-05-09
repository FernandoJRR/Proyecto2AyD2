package com.ayd.config_service.parameters.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.config_service.parameters.models.Parameter;

public interface ParameterRepository extends JpaRepository<Parameter, String>{
    public Optional<Parameter> findOneByParameterKey(String parameterKey);
    public boolean existsByParameterKey(String parameterKey);
}
