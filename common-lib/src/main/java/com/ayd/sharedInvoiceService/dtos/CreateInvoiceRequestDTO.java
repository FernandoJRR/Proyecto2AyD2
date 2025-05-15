package com.ayd.sharedInvoiceService.dtos;

import jakarta.validation.constraints.*;
import lombok.Value;

import java.util.List;

import com.ayd.sharedInvoiceService.enums.PaymentMethod;

@Value
public class CreateInvoiceRequestDTO {
    @NotNull(message = "El método de pago es obligatorio")
    PaymentMethod paymentMethod;

    @NotBlank(message = "El documento del cliente es obligatorio")
    String clientDocument;

    @NotEmpty(message = "La factura debe tener al menos un detalle")
    List<CreateInvoiceDetailRequestDTO> details;
}
