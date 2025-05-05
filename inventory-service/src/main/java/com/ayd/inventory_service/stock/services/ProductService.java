package com.ayd.inventory_service.stock.services;

import com.ayd.inventory_service.stock.dtos.ProductResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ProductService {

    private final WebClient webClient;

    public ProductService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8084/api/").build();
    }

    public ProductResponseDTO getProductById(String productId) {
        String url = "v1/products/" + productId;
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(ProductResponseDTO.class)
                .block();
    }

    public List<ProductResponseDTO> getProductsByIds(List<String> productIds) {
        String url = "v1/products/ids";
        return webClient.post()
                .uri(url)
                .bodyValue(productIds)
                .retrieve()
                .bodyToFlux(ProductResponseDTO.class)
                .collectList()
                .block();
    }
}
