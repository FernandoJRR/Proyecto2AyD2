package com.ayd.invoice_service.Invoice.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.ports.ForInvoiceDetailPort;
import com.ayd.invoice_service.Invoice.repositories.InvoiceDetailRepository;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class InvoiceDetailService implements ForInvoiceDetailPort {

    private final InvoiceDetailRepository invoiceDetailRepository;

    @Override
    public InvoiceDetail getInvoiceDetailById(String id) throws NotFoundException {
        return invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el detalle de la factura con id: " + id));
    }

    @Override
    public InvoiceDetail createInvoiceDetail(CreateInvoiceDetailRequestDTO createInvoiceDetailRequestDTO,
            Invoice invoice) throws IllegalArgumentException {
        // Verifiacamos que la cantidad y el precio unitario sean mayores a 0
        if (createInvoiceDetailRequestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (createInvoiceDetailRequestDTO.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoice(invoice);
        invoiceDetail.setItemId(createInvoiceDetailRequestDTO.getItemId());
        invoiceDetail.setItemName(createInvoiceDetailRequestDTO.getItemName());
        invoiceDetail.setQuantity(createInvoiceDetailRequestDTO.getQuantity());
        invoiceDetail.setUnitPrice(createInvoiceDetailRequestDTO.getUnitPrice());
        invoiceDetail.setTotal(createInvoiceDetailRequestDTO.getUnitPrice()
                .multiply(BigDecimal.valueOf(createInvoiceDetailRequestDTO.getQuantity())));
        invoiceDetail = invoiceDetailRepository.save(invoiceDetail);
        return invoiceDetail;
    }

    @Override
    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(String invoiceId) {
        return invoiceDetailRepository.findByInvoiceId(invoiceId);
    }

    @Override
    public BigDecimal calcValuesInvoiceDetail(List<CreateInvoiceDetailRequestDTO> createInvoiceDetailRequestDTO)
            throws IllegalArgumentException {
        BigDecimal total = BigDecimal.ZERO;
        for (var detail : createInvoiceDetailRequestDTO) {
            if (detail.getQuantity() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
            }
            if (detail.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
            }
            total = total.add(detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
        }
        return total;
    }
}
