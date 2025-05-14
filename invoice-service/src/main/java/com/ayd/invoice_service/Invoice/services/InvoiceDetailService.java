package com.ayd.invoice_service.Invoice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.invoice_service.Invoice.adapter.ProductClientAdapter;
import com.ayd.invoice_service.Invoice.dtos.CreateInvoiceDetailRequestDTO;
import com.ayd.invoice_service.Invoice.enums.ItemType;
import com.ayd.invoice_service.Invoice.models.Invoice;
import com.ayd.invoice_service.Invoice.models.InvoiceDetail;
import com.ayd.invoice_service.Invoice.ports.ForInvoiceDetailPort;
import com.ayd.invoice_service.Invoice.repositories.InvoiceDetailRepository;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedProductService.packages.dtos.GolfPackageResponseDTO;
import com.ayd.sharedProductService.product.dtos.ProductResponseDTO;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional(rollbackOn = Exception.class)
@AllArgsConstructor
public class InvoiceDetailService implements ForInvoiceDetailPort {

    private final InvoiceDetailRepository invoiceDetailRepository;
    private final ProductClientAdapter productClientAdapter;

    @Override
    public InvoiceDetail getInvoiceDetailById(String id) throws NotFoundException {
        return invoiceDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No se encontró el detalle de la factura con id: " + id));
    }

    @Override
    public List<InvoiceDetail> createInvoiceDetail(CreateInvoiceDetailRequestDTO createInvoiceDetailRequestDTO,
            Invoice invoice) throws IllegalArgumentException, NotFoundException {
        // Verifiacamos que la cantidad y el precio unitario sean mayores a 0
        if (createInvoiceDetailRequestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }
        if (createInvoiceDetailRequestDTO.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor a 0");
        }

        List<InvoiceDetail> invoiceDetails = new ArrayList<>();

        // Si el producto es es de tipo servicio de busca el packete
        if (createInvoiceDetailRequestDTO.getItemType() == ItemType.SERVICE) {
            GolfPackageResponseDTO dto = productClientAdapter
                    .getPackageById(createInvoiceDetailRequestDTO.getItemId());
            dto.getPackageDetail().forEach(packageDetail -> {
                InvoiceDetail innerInvoiceDetail = new InvoiceDetail();
                innerInvoiceDetail.setInvoice(invoice);
                innerInvoiceDetail.setItemId(packageDetail.getProduct().getId());
                innerInvoiceDetail.setItemName(packageDetail.getProduct().getName());
                innerInvoiceDetail.setQuantity(createInvoiceDetailRequestDTO.getQuantity());
                innerInvoiceDetail.setUnitPrice(createInvoiceDetailRequestDTO.getUnitPrice());
                innerInvoiceDetail.setTotal(createInvoiceDetailRequestDTO.getUnitPrice()
                        //.multiply(BigDecimal.valueOf(createInvoiceDetailRequestDTO.getQuantity()))
                        .divide(BigDecimal.valueOf(dto.getPackageDetail().size()))
                        .divide(BigDecimal.valueOf(packageDetail.getQuantity())));
                innerInvoiceDetail.setItemType(ItemType.GOOD);
                invoiceDetails.add(innerInvoiceDetail);
            });
        } else {
            InvoiceDetail invoiceDetail = new InvoiceDetail();
            ProductResponseDTO dto = productClientAdapter
                    .getProductById(createInvoiceDetailRequestDTO.getItemId());
            invoiceDetail.setInvoice(invoice);
            invoiceDetail.setItemId(dto.getId());
            invoiceDetail.setItemName(dto.getName());
            invoiceDetail.setQuantity(createInvoiceDetailRequestDTO.getQuantity());
            invoiceDetail.setUnitPrice(createInvoiceDetailRequestDTO.getUnitPrice());
            invoiceDetail.setTotal(createInvoiceDetailRequestDTO.getUnitPrice()
                    .multiply(BigDecimal.valueOf(createInvoiceDetailRequestDTO.getQuantity())));
            invoiceDetail = invoiceDetailRepository.save(invoiceDetail);
            invoiceDetail.setItemType(ItemType.GOOD);
            invoiceDetails.add(invoiceDetail);
        }

        return invoiceDetails;
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
