package com.ayd.product_service.product.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.DeleteProductResponseDTO;
import com.ayd.product_service.product.dtos.ProductResponseDTO;
import com.ayd.product_service.product.dtos.SpecificationProductDTO;
import com.ayd.product_service.product.dtos.StateProductResponseDTO;
import com.ayd.product_service.product.dtos.TypeProductResponseDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.mappers.ProductMapper;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.ports.ForProductPort;
import com.ayd.shared.exceptions.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los datos de un producto identificado por su ID. Valida duplicados y existencia previa del producto.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud (validación de campos)"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe otro producto con los mismos datos"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDTO updateProduct(
            @PathVariable String id,
            @Valid @RequestBody UpdateProductRequestDTO updateProductRequestDTO)
            throws DuplicatedEntryException, NotFoundException {
        Product product = forProductPort.updateProduct(id, updateProductRequestDTO);
        return productMapper.fromProductToProductResponseDTO(product);
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del sistema utilizando su ID. Si el producto no existe, devuelve un error.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DeleteProductResponseDTO deleteProduct(@PathVariable String id) throws NotFoundException {
        boolean rssult = forProductPort.deleteProduct(id);
        return new DeleteProductResponseDTO(id, rssult, "Producto con id: " + id+" eliminado correctamente");
    }

    @Operation(summary = "Obtener un producto por ID", description = "Devuelve la información de un producto a partir de su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponseDTO getProductById(@PathVariable String id) throws NotFoundException {
        Product product = forProductPort.getProduct(id);
        return productMapper.fromProductToProductResponseDTO(product);
    }

    @Operation(summary = "Obtener todos los productos", description = "Devuelve una lista de productos. Puede recibir filtros opcionales en el cuerpo de la solicitud para realizar búsquedas más específicas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getAllProducts(
            @RequestBody(required = false) SpecificationProductDTO specificationProductDTO) {
        List<Product> products = forProductPort.getProducts(specificationProductDTO);
        return productMapper.fromProductsToProductResponseDTOs(products);
    }

    @Operation(summary = "Obtener productos por lista de IDs", description = "Devuelve una lista de productos cuyos IDs coincidan con los proporcionados en el cuerpo de la solicitud.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @PostMapping("/ids")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponseDTO> getProductsByIds(
            @RequestBody(required = false) List<String> ids) {
        List<Product> products = forProductPort.getProductsByIds(ids);
        return productMapper.fromProductsToProductResponseDTOs(products);
    }

    @Operation(summary = "Obtener estados de producto", description = "Devuelve una lista de todos los estados posibles que puede tener un producto en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estados obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/states")
    @ResponseStatus(HttpStatus.OK)
    public List<StateProductResponseDTO> getStates() {
        return forProductPort.getStates();
    }

    @Operation(summary = "Obtener tipos de producto", description = "Devuelve una lista con los tipos de producto disponibles en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error inesperado del servidor")
    })
    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    public List<TypeProductResponseDTO> getTypes() {
        return forProductPort.getTypes();
    }
}
