package com.ayd.employee_service.auth.jwt.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtManagerUtil {

    /**
     * Extrae y devuelve el nombre de usuario del token jwt.
     *
     * @param token el token jwt del cual se extraerá el nombre de usuario.
     * @return el nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) throws JwtException, IllegalArgumentException {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Extrae y devuelve la fecha de expiración del token jwt.
     *
     * @param token el token jwt del cual se extraerá la fecha de expiración.
     * @return la fecha de expiración contenida en el token.
     */
    public Date extractExpiration(String token) throws JwtException, IllegalArgumentException {
        return extractAllClaims(token).getExpiration();
    }

    /**
     * Valida si un token jwt es valido comparando el nombre de usuario extraído
     * y verificando que el token no haya expirado.
     *
     * @param token    el token jwt a validar.
     * @param username el nombre de usuario esperado.
     * @return true si el token es válido, false en caso contrario.
     */
    public boolean isTokenValid(String token, String username) throws JwtException, IllegalArgumentException {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    /**
     * Extrae y devuelve los claims de un token jwt firmado.
     *
     * @param token el token jwt a analizar.
     * @return los claims contenidos en el token.
     */
    private Claims extractAllClaims(String token) throws JwtException, IllegalArgumentException {
        // crea un parser para validar el jwt
        return Jwts.parser()
                // aqui se verifica el jwt con la llave secretea
                .verifyWith(Keys.hmacShaKeyFor(JwtGeneratorUtil.SECRET_KEY.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Verifica si expiracion del token es antes que la fecha actual
     *
     * @param token
     * @return
     */
    public boolean isTokenExpired(String token) throws JwtException, IllegalArgumentException {
        return extractExpiration(token).before(new Date());
    }
}
