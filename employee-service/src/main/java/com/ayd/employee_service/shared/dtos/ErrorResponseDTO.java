package com.ayd.employee_service.shared.dtos;

import lombok.Value;

/**
 * Esta clase la cree para evitar el error del texto plano en los Expcetion en
 * el global exception hanlder
 */
@Value
public class ErrorResponseDTO {

    String message;
}
