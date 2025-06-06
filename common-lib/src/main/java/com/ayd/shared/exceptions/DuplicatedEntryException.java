package com.ayd.shared.exceptions;

/**
 * Exception que indica que ya existe con registro con la data especificada.
 * 
 * @author Luis Monterroso
 */
public class DuplicatedEntryException extends Exception {

    public DuplicatedEntryException(String message) {
        super(message);
    }

}
