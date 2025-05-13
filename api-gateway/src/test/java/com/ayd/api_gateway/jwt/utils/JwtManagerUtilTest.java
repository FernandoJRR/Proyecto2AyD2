package com.ayd.api_gateway.jwt.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtManagerUtilTest {

    private static final String USERNAME = "testuser";
    private static final String SECRET_KEY = JwtManagerUtil.SECRET_KEY;
    private static final List<String> AUTHORITIES = List.of("ROLE_USER", "ROLE_ADMIN");

    private JwtManagerUtil jwtManagerUtil;
    private String validToken;

    @BeforeEach
    void setUp() {
        jwtManagerUtil = new JwtManagerUtil();

        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

        validToken = Jwts.builder()
                .subject(USERNAME)
                .claim("authorities", AUTHORITIES)
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(key)
                .compact();

    }

    /**
     * dado: token válido
     * cuando: se extrae username
     * entonces: retorna username correcto
     */
    @Test
    void extractUsernameReturnsCorrectValue() {
        String result = jwtManagerUtil.extractUsername(validToken);
        assertEquals(USERNAME, result);
    }

    /**
     * dado: token válido
     * cuando: se extraen permisos
     * entonces: retorna permisos concatenados
     */
    @Test
    void extractPermissionsReturnsCorrectValue() {
        String result = jwtManagerUtil.extractPermissons(validToken);
        assertEquals("ROLE_USER,ROLE_ADMIN", result);
    }

    /**
     * dado: token válido
     * cuando: se extrae expiración
     * entonces: retorna fecha futura
     */
    @Test
    void extractExpirationReturnsFutureDate() {
        Date expiration = jwtManagerUtil.extractExpiration(validToken);
        assertTrue(expiration.after(new Date()));
    }

    /**
     * dado: token válido
     * cuando: se valida expiración
     * entonces: retorna false (no expirado)
     */
    @Test
    void isTokenExpiredReturnsFalse() {
        assertFalse(jwtManagerUtil.isTokenExpired(validToken));
    }

    /**
     * dado: token válido
     * cuando: se valida token
     * entonces: retorna true
     */
    @Test
    void isTokenValidReturnsTrue() {
        assertTrue(jwtManagerUtil.isTokenValid(validToken));
    }

}
