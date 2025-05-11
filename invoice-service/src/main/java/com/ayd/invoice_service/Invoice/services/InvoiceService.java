package com.ayd.invoice_service.Invoice.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.dtos.SpecificationInvoiceRequestDTO;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.ports.ForInvoiceDetailPort;
import com.ayd.invoice_service.Invoice.ports.ForInvoicePort;
import com.ayd.invoice_service.Invoice.repositories.InvoiceRepository;
import com.ayd.invoice_service.Invoice.specifications.InvoiceSpecification;
import com.ayd.shared.exceptions.NotFoundException;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class InvoiceService implements ForInvoicePort {

    private final InvoiceRepository invoiceRepository;
    private final ForInvoiceDetailPort forInvoiceDetailPort;

    @Override
    public Invoice createInvoice(CreateInvoiceRequestDTO createInvoiceRequestDTO)
            throws IllegalArgumentException, NotFoundException {
        // Verificamos quue el modelo de creacion tenga detalles
        if (createInvoiceRequestDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("La factura debe tener al menos un detalle");
        }
        Invoice invoice = new Invoice();

        invoice.setPaymentMethod(createInvoiceRequestDTO.getPaymentMethod());
        invoice.setClientDocument(createInvoiceRequestDTO.getClientDocument());

        BigDecimal total = forInvoiceDetailPort.calcValuesInvoiceDetail(createInvoiceRequestDTO.getDetails());

        invoice.setSubtotal(total);
        BigDecimal MyCompaytax = BigDecimal.valueOf(0.12);
        BigDecimal tax = invoice.getSubtotal().multiply(MyCompaytax);
        invoice.setTotal(total.add(tax));

        Invoice saveInvoice = invoiceRepository.save(invoice);

        List<InvoiceDetail> invoiceDetails = List.of();
        for (var detail : createInvoiceRequestDTO.getDetails()) {
            invoiceDetails.add(forInvoiceDetailPort.createInvoiceDetail(detail, invoice));
        }

        return invoiceRepository.findById(saveInvoice.getId())
                .orElseThrow(() -> new NotFoundException("No se encontró la factura con id: " + saveInvoice.getId()));
    }

    @Override
    public Invoice getInvoiceById(String id) throws NotFoundException {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró la factura con id: " + id));
    }

    @Override
    public List<Invoice> getInvoicesByClientDocument(String clientDocument) {
        return invoiceRepository.findByClientDocument(clientDocument);
    }

    @Override
    public List<Invoice> getAllInvoices(SpecificationInvoiceRequestDTO specificationInvoiceRequestDTO) {
        if (specificationInvoiceRequestDTO == null) {
            return invoiceRepository.findAll();
        }
        Specification<Invoice> spec = Specification
                .where(InvoiceSpecification.hasClientDocument(specificationInvoiceRequestDTO.getClientDocument()))
                .and(InvoiceSpecification.hasPaymentMethod(specificationInvoiceRequestDTO.getPaymentMethod()));
        return invoiceRepository.findAll(spec);
    }

}
