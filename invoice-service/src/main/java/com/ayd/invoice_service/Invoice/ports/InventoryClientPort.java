package com.ayd.invoice_service.Invoice.ports;

import java.util.List;

import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedInventoryService.cashRegister.dto.CashRegisterResponseDTO;
import com.ayd.sharedInventoryService.stock.dto.ModifyStockRequest;
import com.ayd.sharedInventoryService.stock.dto.StockResponseDTO;

public interface InventoryClientPort {
    public List<StockResponseDTO> substractVariousStockByProductIdAndWarehouseId(
            List<ModifyStockRequest> modifyStockRequests) throws NotFoundException, IllegalStateException;

    public CashRegisterResponseDTO findByEmployeeId(String employeeId) throws NotFoundException;
}
