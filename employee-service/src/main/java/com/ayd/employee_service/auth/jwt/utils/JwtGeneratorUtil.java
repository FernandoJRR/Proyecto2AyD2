package com.ayd.employee_service.auth.jwt.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.ayd.employee_service.auth.jwt.ports.ForJwtGenerator;
import com.ayd.employee_service.users.models.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtGeneratorUtil implements ForJwtGenerator {

    public static final long JWT_TOKEN_TIME_VALIDITY = 86400000; // 1 día en milisegundos
    public static final String SECRET_KEY = "claveSercretisimaXdLuisMonterroso";

    @Override
    public String generateToken(User user, Set<GrantedAuthority> permissions) {
        // Extraer roles y permisos
        Map<String, Object> claims = new HashMap<>();

        // Agregar claims personalizados
        claims.put("rol", "ROLE_USER");
        claims.put("authorities", permissions);

        // Generar el token
        return createToken(claims, user.getUsername());
    }

    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TOKEN_TIME_VALIDITY))
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

}
