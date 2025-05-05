package com.ayd.employee_service.users.ports;

import com.ayd.employee_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.employee_service.shared.exceptions.NotFoundException;
import com.ayd.employee_service.users.models.User;

public interface ForUsersPort {

    public User createUser(User newUser) throws DuplicatedEntryException;

    public User findUserById(String userId) throws NotFoundException;

    public User findUserByUsername(String username) throws NotFoundException;

}
