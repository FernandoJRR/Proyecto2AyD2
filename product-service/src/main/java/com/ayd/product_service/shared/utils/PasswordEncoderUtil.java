
package com.ayd.product_service.shared.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adaptador para la encriptación de contraseñas utilizando BCrypt.
 *
 */
@RequiredArgsConstructor
@Component
public class PasswordEncoderUtil {

    private final PasswordEncoder encoder;

    /**
     * Genera un hash seguro para la contraseña en texto plano proporcionada.
     *
     * @param rawPassword La contraseña en texto plano que se desea encriptar.
     * @return Un hash seguro generado usando BCrypt.
     */
    public String encode(String rawPassword) {
        return encoder.encode(rawPassword); // Genera el hash seguro
    }

    /**
     * Verifica si una contraseña en texto plano coincide con su versión
     * encriptada.
     *
     * @param rawPassword     La contraseña en texto plano proporcionada por el
     *                        usuario.
     * @param encodedPassword El hash seguro almacenado en la base de datos.
     * @return {@code true} si la contraseña coincide, de lo contrario
     *         {@code false}.
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword); // Comparación segura
    }

}
