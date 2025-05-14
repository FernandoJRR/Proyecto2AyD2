package com.ayd.invoice_service.Invoice.ports;

import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedConfigService.dto.ParameterResponseDTO;

public interface ConfigClientPort {
    public ParameterResponseDTO getRegimenParameter() throws NotFoundException;
}
