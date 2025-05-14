package com.ayd.invoice_service.Invoice.adapter;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.ayd.invoice_service.Invoice.ports.InventoryClientPort;
import com.ayd.shared.exceptions.NotFoundException;
import com.ayd.sharedInventoryService.stock.dto.ModifyStockRequest;
import com.ayd.sharedInventoryService.stock.dto.StockResponseDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InventoryClientAdapter implements InventoryClientPort {

    private final WebClient.Builder webClientBuilder;

    @Override
    public List<StockResponseDTO> substractVariousStockByProductIdAndWarehouseId(
            List<ModifyStockRequest> modifyStockRequests) throws NotFoundException, IllegalStateException {
        return webClientBuilder.build()
                .post()
                .uri("lb://API-GATEWAY/api/v1/stocks/modify-stock")
                .bodyValue(modifyStockRequests)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(message -> new NotFoundException(message)))
                .onStatus(status -> status.value() == 409,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(message -> new IllegalStateException(message)))
                .bodyToFlux(StockResponseDTO.class)
                .collectList()
                .block();
    }
}
