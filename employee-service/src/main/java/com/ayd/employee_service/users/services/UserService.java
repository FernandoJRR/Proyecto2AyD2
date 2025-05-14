package com.ayd.employee_service.users.services;

import org.springframework.stereotype.Service;

import com.ayd.shared.exceptions.*;
import com.ayd.employee_service.shared.utils.PasswordEncoderUtil;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.ports.ForUsersPort;
import com.ayd.employee_service.users.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements ForUsersPort {

    private final UserRepository userRepository;
    private final PasswordEncoderUtil passwordEncoderUtil;

    @Transactional(rollbackOn = Exception.class)
    public User createUser(User newUser) throws DuplicatedEntryException {
        // verificamos si el nombre de usuario existe ya en l bd
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new DuplicatedEntryException(
                    "Ya existe un usuario con el mismo nombre de usuario.");
        }

        // mandamos a encriptar la password
        newUser.setPassword(passwordEncoderUtil.encode(newUser.getPassword()));

        // guardar el usuario
        User save = userRepository.save(newUser);

        return save;
    }

    public User findUserById(String userId) throws NotFoundException {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException(
                "El id especificado no pertenece a ningun usuario en el sistema"));
    }

    public User findUserByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(
                "El nombre de usuario especificado no pertenece a ningun usuario en el sistema"));
    }

}
