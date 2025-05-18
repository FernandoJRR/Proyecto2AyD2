package com.ayd.employee_service.users.services;

import com.ayd.employee_service.shared.utils.PasswordEncoderUtil;
import com.ayd.employee_service.users.models.User;
import com.ayd.employee_service.users.repositories.UserRepository;
import com.ayd.shared.exceptions.DuplicatedEntryException;
import com.ayd.shared.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderUtil passwordEncoderUtil;

    @InjectMocks
    private UserService userService;

    private static final String USER_ID = "user-001";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";
    private static final String ENCRYPTED_PASSWORD = "encrypted123";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
    }

    /**
     * dado: que el nombre de usuario no está duplicado.
     * cuando: se llama a createUser.
     * entonces: se guarda correctamente y se encripta la contraseña.
     */
    @Test
    public void createUserShouldSucceedWhenUsernameNotExists() throws DuplicatedEntryException {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(passwordEncoderUtil.encode(PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals(ENCRYPTED_PASSWORD, result.getPassword());
        verify(userRepository).save(user);
    }

    /**
     * dado: que el nombre de usuario ya existe.
     * cuando: se llama a createUser.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void createUserShouldThrowWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);

        assertThrows(DuplicatedEntryException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    /**
     * dado: que el ID existe.
     * cuando: se llama a findUserById.
     * entonces: se retorna el usuario correspondiente.
     */
    @Test
    public void findUserByIdShouldReturnWhenExists() throws NotFoundException {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        User result = userService.findUserById(USER_ID);

        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userRepository).findById(USER_ID);
    }

    /**
     * dado: que el ID no existe.
     * cuando: se llama a findUserById.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findUserByIdShouldThrowWhenNotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserById(USER_ID));
    }

    /**
     * dado: que el nombre de usuario existe.
     * cuando: se llama a findUserByUsername.
     * entonces: se retorna el usuario correspondiente.
     */
    @Test
    public void findUserByUsernameShouldReturnWhenExists() throws NotFoundException {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        User result = userService.findUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
        verify(userRepository).findByUsername(USERNAME);
    }

    /**
     * dado: que el nombre de usuario no existe.
     * cuando: se llama a findUserByUsername.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void findUserByUsernameShouldThrowWhenNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findUserByUsername(USERNAME));
    }
}
