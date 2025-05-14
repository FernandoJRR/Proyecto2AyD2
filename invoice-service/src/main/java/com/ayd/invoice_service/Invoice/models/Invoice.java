package com.ayd.invoice_service.Invoice.models;

import java.math.BigDecimal;
import java.util.List;

import com.ayd.invoice_service.Invoice.enums.PaymentMethod;
import com.ayd.shared.models.Auditor;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invoice extends Auditor {

    private PaymentMethod paymentMethod;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal total;

    private String clientDocument; // CUI/NIT // ID del cliente

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceDetail> details;
}
