package com.ayd.inventory_service.stock.services;

import com.ayd.inventory_service.shared.exceptions.NotFoundException;
import com.ayd.inventory_service.stock.dtos.StockResponseDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class ProductService {

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8084/api/").build();
    }

    public StockResponseDTO getProductById(String productId) throws NotFoundException {
        String url = "v1/products/" + productId;
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(StockResponseDTO.class)
                    .block();
        } catch (WebClientResponseException e){
            if (e.getStatusCode().is4xxClientError()) {
                throw new NotFoundException("No se encontró el producto");
            } else if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Error en el servicio de productos");
            }else {
                throw new RuntimeException("Error en el servicio de inventario");
            }
        }
    }

    public List<StockResponseDTO> getProductsByIds(List<String> productIds){
        String url = "v1/products/ids";
        try{
            return webClient.post()
                    .uri(url)
                    .bodyValue(productIds)
                    .retrieve()
                    .bodyToFlux(StockResponseDTO.class)
                    .collectList()
                    .block();
        } catch ( WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                throw new RuntimeException("Error en el servicio de productos");
            } else {
                throw new RuntimeException("Error en el servicio de inventario");
            }
        }
    }
}
