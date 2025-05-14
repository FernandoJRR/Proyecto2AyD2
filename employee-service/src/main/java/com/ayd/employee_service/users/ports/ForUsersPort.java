package com.ayd.employee_service.users.ports;

import com.ayd.shared.exceptions.*;
import com.ayd.employee_service.users.models.User;

public interface ForUsersPort {

    public User createUser(User newUser) throws DuplicatedEntryException;

    public User findUserById(String userId) throws NotFoundException;

    public User findUserByUsername(String username) throws NotFoundException;
}
