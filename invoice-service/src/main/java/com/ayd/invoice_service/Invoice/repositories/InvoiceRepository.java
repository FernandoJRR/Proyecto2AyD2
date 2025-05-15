package com.ayd.invoice_service.Invoice.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ayd.invoice_service.Invoice.models.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, String>, JpaSpecificationExecutor<Invoice> {
    List<Invoice> findByClientDocument(String clientDocument);

    public List<Invoice> findAllByIdIn(List<String> ids);
}
