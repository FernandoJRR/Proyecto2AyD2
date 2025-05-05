package com.ayd.employee_service.users.mappers;

import org.mapstruct.Mapper;

import com.ayd.employee_service.users.dtos.CreateUserRequestDTO;
import com.ayd.employee_service.users.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    public User fromCreateUserRequestDtoToUser(CreateUserRequestDTO dto);
}
