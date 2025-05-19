package com.ayd.employee_service.shared.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PasswordEncoderUtilTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    private PasswordEncoderUtil passwordEncoderUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoderUtil = new PasswordEncoderUtil(passwordEncoder);
    }

    /**
     * dado: una contraseña en texto plano.
     * cuando: se llama a encode.
     * entonces: se retorna el hash generado por el encoder.
     */
    @Test
    public void encodeShouldReturnEncodedPassword() {
        String rawPassword = "1234";
        String encoded = "hashed1234";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encoded);

        String result = passwordEncoderUtil.encode(rawPassword);

        assertEquals(encoded, result);
        verify(passwordEncoder).encode(rawPassword);
    }

    /**
     * dado: contraseña y hash que coinciden.
     * cuando: se llama a matches.
     * entonces: se retorna true.
     */
    @Test
    public void matchesShouldReturnTrueWhenMatch() {
        String rawPassword = "1234";
        String encoded = "hashed1234";

        when(passwordEncoder.matches(rawPassword, encoded)).thenReturn(true);

        boolean result = passwordEncoderUtil.matches(rawPassword, encoded);

        assertTrue(result);
        verify(passwordEncoder).matches(rawPassword, encoded);
    }

    /**
     * dado: contraseña y hash que no coinciden.
     * cuando: se llama a matches.
     * entonces: se retorna false.
     */
    @Test
    public void matchesShouldReturnFalseWhenNotMatch() {
        String rawPassword = "wrong";
        String encoded = "hashed1234";

        when(passwordEncoder.matches(rawPassword, encoded)).thenReturn(false);

        boolean result = passwordEncoderUtil.matches(rawPassword, encoded);

        assertFalse(result);
        verify(passwordEncoder).matches(rawPassword, encoded);
    }
}
