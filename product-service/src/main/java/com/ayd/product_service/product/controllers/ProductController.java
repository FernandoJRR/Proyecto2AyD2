package com.ayd.product_service.product.controllers;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.ProductResponseDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.mappers.ProductMapper;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.ports.ForProductPort;
import com.ayd.product_service.shared.exceptions.DuplicatedEntryException;
import com.ayd.product_service.shared.exceptions.NotFoundException;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ForProductPort forProductPort;
    private final ProductMapper productMapper;

    @Operation(summary = "Creación de un nuevo producto.", description = "Este endpoint permite crear un nuevo producto. Valida que no exista un producto con el mismo nombre u otro campo único, y que las entidades relacionadas existan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error de validación en los datos de entrada"),
            @ApiResponse(responseCode = "404", description = "Entidad relacionada no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe un producto con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })

    
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDTO createProduct(@Valid @RequestBody CreateProductRequestDTO createProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Product product = forProductPort.createProduct(createProductRequestDTO);
        return productMapper.fromProductToProductResponseDTO(product);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDTO updateProduct(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductRequestDTO updateProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Product product = forProductPort.updateProduct(id, updateProductRequestDTO);
        return productMapper.fromProductToProductResponseDTO(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean deleteProduct(@PathVariable String id) throws NotFoundException {
        return forProductPort.deleteProduct(id);
    }

}
