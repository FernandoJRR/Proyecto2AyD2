package com.ayd.reports_service.shared.ports;

import java.util.List;

import com.ayd.sharedInvoiceService.dtos.InvoiceResponseDTO;

public interface InvoiceClientPort {

    public List<InvoiceResponseDTO> getProfitsByIds(List<String> ids);
}
