package com.ayd.invoice_service.Invoice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ayd.invoice_service.Invoice.models.InvoiceDetail;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, String> {
    List<InvoiceDetail> findByInvoiceId(String invoiceId);
}
