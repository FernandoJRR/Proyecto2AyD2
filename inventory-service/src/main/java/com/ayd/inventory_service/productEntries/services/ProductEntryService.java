package com.ayd.inventory_service.productEntries.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ayd.inventory_service.productEntries.dtos.ProductEntryDetailRequestDTO;
import com.ayd.inventory_service.productEntries.dtos.ProductEntryRequestDTO;
import com.ayd.inventory_service.productEntries.models.ProductEntry;
import com.ayd.inventory_service.productEntries.ports.ForProductEntryDetailPort;
import com.ayd.inventory_service.productEntries.ports.ForProductEntryPort;
import com.ayd.inventory_service.productEntries.repositories.ProductEntryRepository;
import com.ayd.inventory_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.ports.ForStockPort;
import com.ayd.inventory_service.supplier.models.Supplier;
import com.ayd.inventory_service.supplier.ports.ForSupplierPort;
import com.ayd.inventory_service.warehouse.models.Warehouse;
import com.ayd.inventory_service.warehouse.ports.ForWarehousePort;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class ProductEntryService implements ForProductEntryPort {

    private final ProductEntryRepository productEntryRepository;
    private final ForProductEntryDetailPort forProductEntryDetailPort;
    private final ForStockPort forStockPort;
    private final ForWarehousePort forWarehousePort;
    private final ForSupplierPort forSupplierPort;

    @Override
    public ProductEntry getProductEntryById(String id) throws NotFoundException {
        return productEntryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("La entrada de producto no existe"));
    }

    @Override
    public ProductEntry getProductEntryByInvoiceNumber(String invoiceNumber) throws NotFoundException {
        return productEntryRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new NotFoundException("La entrada de producto no existe"));
    }

    @Override
    public ProductEntry saveProductEntry(ProductEntryRequestDTO productEntryRequestDTO)
            throws NotFoundException, DuplicatedEntryException,IllegalStateException {
        Warehouse warehouse = forWarehousePort.getWarehouse(productEntryRequestDTO.getWarehouseId());
        Supplier supplier = forSupplierPort.getSupplierById(productEntryRequestDTO.getSupplierId());
        if (productEntryRepository.existsByInvoiceNumber(productEntryRequestDTO.getInvoiceNumber())) {
            throw new DuplicatedEntryException("El numero de factura ya existe");
        }
        ProductEntry productEntry = new ProductEntry(productEntryRequestDTO, warehouse, supplier);
        productEntry = productEntryRepository.save(productEntry);
        for (ProductEntryDetailRequestDTO productEntryDetailRequestDTO : productEntryRequestDTO.getDetails()) {
            forProductEntryDetailPort.saveProductEntryDetail(productEntryDetailRequestDTO, productEntry);
            forStockPort.addStockByProductIdAndWarehouseId(productEntryDetailRequestDTO.getProductId(), 
                    productEntry.getWarehouse(),productEntryDetailRequestDTO.getQuantity());
        }
        productEntry = getProductEntryById(productEntry.getId());
        return productEntry;
    }

    @Override
    public List<ProductEntry> getAllProductEntries() {
        return productEntryRepository.findAll();
    }

}
