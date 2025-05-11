package com.ayd.shared.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ayd.shared.dtos.ErrorResponseDTO;
import com.ayd.shared.exceptions.*;
import jakarta.validation.ConstraintViolationException;

/**
 *
 * @author Luis Monterroso
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedEntryException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleDuplicatedEntryException(DuplicatedEntryException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDTO handleNotFoundException(IllegalStateException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(ReportGenerationExeption.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleConstraintViolationException(ReportGenerationExeption ex) {
        return new ErrorResponseDTO("Error inesperado al generar el reporte. " + ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleConstraintViolationException(ConstraintViolationException ex) {
        return new ErrorResponseDTO("Error inesperado en la Base de Datos.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        String menssage = "";
        // recorre los errores de validación y agregarlos al mensaje de respuesta
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            menssage = menssage + String.format("-%s ", error.getDefaultMessage());
        }
        return new ErrorResponseDTO(menssage);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDTO handleBadCredentialsException(BadCredentialsException ex) {
        return new ErrorResponseDTO("Autenticación fallida, El correo electrónico o la contraseña son incorrectos."
                + " Por favor, verifica tus credenciales e intenta de nuevo.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDTO handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponseDTO("Acceso denegado, no tienes permisos suficientes para realizar esta acción.");
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ErrorResponseDTO(
                "El nombre de usuario especificado no pertenece a ningun usuario dentro del sistema.");

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleNotFoundException(NotFoundException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(InvalidPeriodException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO handleInvalidPeriodException(InvalidPeriodException ex) {
        return new ErrorResponseDTO(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDTO handleGlobalException(Exception ex) {
        ex.printStackTrace();
        return new ErrorResponseDTO("Ocurrió un error inesperado: " + ex.getMessage());
    }

}
