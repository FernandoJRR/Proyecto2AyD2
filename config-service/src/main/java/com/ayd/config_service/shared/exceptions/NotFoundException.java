
package com.ayd.config_service.shared.exceptions;

/**
 * Exepcion que indica que un recurso no fue encontrado dentro de la api.
 *
 * @author Luis Monterroso
 */
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }

}
