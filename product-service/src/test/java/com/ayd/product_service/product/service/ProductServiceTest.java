package com.ayd.product_service.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import com.ayd.product_service.product.dtos.CreateProductRequestDTO;
import com.ayd.product_service.product.dtos.UpdateProductRequestDTO;
import com.ayd.product_service.product.dtos.SpecificationProductDTO;
import com.ayd.product_service.product.dtos.StateProductResponseDTO;
import com.ayd.product_service.product.dtos.TypeProductResponseDTO;
import com.ayd.product_service.product.emuns.EnumProductState;
import com.ayd.product_service.product.emuns.EnumProductType;
import com.ayd.product_service.product.models.Product;
import com.ayd.product_service.product.repositories.ProductRepository;
import com.ayd.product_service.product.services.ProductService;
import com.ayd.shared.exceptions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private static final String PRODUCT_ID = "abc-123";
    private static final String NAME = "Paracetamol";
    private static final String CODE = "MED-001";
    private static final String BAR_CODE = "1234567890123";
    private static final BigDecimal PRICE = BigDecimal.valueOf(10.99);
    private static final EnumProductType TYPE = EnumProductType.GOOD;
    private static final EnumProductState STATE = EnumProductState.ACTIVE;

    private static final String UPDATED_NAME = "Ibuprofeno";
    private static final String UPDATED_CODE = "MED-002";
    private static final String UPDATED_BAR_CODE = "9876543210987";
    private static final BigDecimal UPDATED_PRICE = BigDecimal.valueOf(12.99);
    private static final EnumProductType UPDATED_TYPE = EnumProductType.SERVICE;
    private static final EnumProductState UPDATED_STATE = EnumProductState.INACTIVE;

    private CreateProductRequestDTO createProductRequestDTO;
    private UpdateProductRequestDTO updateProductRequestDTO;
    private Product product;

    @BeforeEach
    void setUp() {
        createProductRequestDTO = new CreateProductRequestDTO(NAME, CODE, BAR_CODE, PRICE, TYPE, STATE);
        updateProductRequestDTO = new UpdateProductRequestDTO(UPDATED_NAME, UPDATED_CODE, UPDATED_BAR_CODE,
                UPDATED_PRICE, UPDATED_TYPE, UPDATED_STATE);
        product = new Product(NAME, CODE, BAR_CODE, PRICE, TYPE, STATE);
        product.setId(PRODUCT_ID);
    }

    /**
     * dado: que no existe ningún producto con el mismo nombre, código o código de
     * barras.
     * cuando: se llama al método createProduct.
     * entonces: se guarda el producto correctamente y se retorna la instancia
     * creada.
     */
    @Test
    public void createProductShouldCreateProductWhenNoDuplicateExists()
            throws DuplicatedEntryException, NotFoundException {
        // Arrange
        when(productRepository.existsByName(NAME)).thenReturn(false);
        when(productRepository.existsByCode(CODE)).thenReturn(false);
        when(productRepository.existsByBarCode(BAR_CODE)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.createProduct(createProductRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(NAME, result.getName()),
                () -> assertEquals(CODE, result.getCode()),
                () -> assertEquals(BAR_CODE, result.getBarCode()),
                () -> assertEquals(TYPE, result.getType()),
                () -> assertEquals(STATE, result.getState()));

        verify(productRepository).existsByName(NAME);
        verify(productRepository).existsByCode(CODE);
        verify(productRepository).existsByBarCode(BAR_CODE);
        verify(productRepository).save(any(Product.class));
    }

    /**
     * dado: que ya existe un producto con el mismo nombre.
     * cuando: se llama al método createProduct.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guarda el
     * producto.
     */
    @Test
    public void createProductShouldThrowWhenNameAlreadyExists() {
        // Arrange
        when(productRepository.existsByName(NAME)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> productService.createProduct(createProductRequestDTO));

        verify(productRepository).existsByName(NAME);
        verify(productRepository, never()).existsByCode(any());
        verify(productRepository, never()).existsByBarCode(any());
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que ya existe un producto con el mismo código.
     * cuando: se llama al método createProduct.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guarda el
     * producto.
     */
    @Test
    public void createProductShouldThrowWhenCodeAlreadyExists() {
        // Arrange
        when(productRepository.existsByName(NAME)).thenReturn(false);
        when(productRepository.existsByCode(CODE)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> productService.createProduct(createProductRequestDTO));

        verify(productRepository).existsByName(NAME);
        verify(productRepository).existsByCode(CODE);
        verify(productRepository, never()).existsByBarCode(any());
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que ya existe un producto con el mismo código de barras.
     * cuando: se llama al método createProduct.
     * entonces: se lanza una excepción DuplicatedEntryException y no se guarda el
     * producto.
     */
    @Test
    public void createProductShouldThrowWhenBarCodeAlreadyExists() {
        // Arrange
        when(productRepository.existsByName(NAME)).thenReturn(false);
        when(productRepository.existsByCode(CODE)).thenReturn(false);
        when(productRepository.existsByBarCode(BAR_CODE)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class, () -> productService.createProduct(createProductRequestDTO));

        verify(productRepository).existsByName(NAME);
        verify(productRepository).existsByCode(CODE);
        verify(productRepository).existsByBarCode(BAR_CODE);
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que existe un producto con el ID proporcionado y no hay duplicados.
     * cuando: se llama al método updateProduct.
     * entonces: se actualizan los datos correctamente y se guarda el producto
     * actualizado.
     */
    @Test
    public void updateProductShouldUpdateSuccessfullyWhenNoDuplicates()
            throws DuplicatedEntryException, NotFoundException {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID)).thenReturn(false);
        when(productRepository.existsByCodeAndIdIsNot(UPDATED_CODE, PRODUCT_ID)).thenReturn(false);
        when(productRepository.existsByBarCodeAndIdIsNot(UPDATED_BAR_CODE, PRODUCT_ID)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Product result = productService.updateProduct(PRODUCT_ID, updateProductRequestDTO);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(UPDATED_NAME, result.getName()),
                () -> assertEquals(UPDATED_CODE, result.getCode()),
                () -> assertEquals(UPDATED_BAR_CODE, result.getBarCode()),
                () -> assertEquals(UPDATED_TYPE, result.getType()),
                () -> assertEquals(UPDATED_STATE, result.getState()));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID);
        verify(productRepository).existsByCodeAndIdIsNot(UPDATED_CODE, PRODUCT_ID);
        verify(productRepository).existsByBarCodeAndIdIsNot(UPDATED_BAR_CODE, PRODUCT_ID);
        verify(productRepository).save(any(Product.class));
    }

    /**
     * dado: que no existe un producto con el ID dado.
     * cuando: se llama al método updateProduct.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void updateProductShouldThrowWhenProductNotFound() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.updateProduct(PRODUCT_ID, updateProductRequestDTO));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que ya existe otro producto con el mismo nombre.
     * cuando: se actualiza un producto.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void updateProductShouldThrowWhenNameAlreadyExistsInOtherProduct() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> productService.updateProduct(PRODUCT_ID, updateProductRequestDTO));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID);
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que ya existe otro producto con el mismo código.
     * cuando: se actualiza un producto.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void updateProductShouldThrowWhenCodeAlreadyExistsInOtherProduct() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID)).thenReturn(false);
        when(productRepository.existsByCodeAndIdIsNot(UPDATED_CODE, PRODUCT_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> productService.updateProduct(PRODUCT_ID, updateProductRequestDTO));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).existsByCodeAndIdIsNot(UPDATED_CODE, PRODUCT_ID);
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que ya existe otro producto con el mismo código de barras.
     * cuando: se actualiza un producto.
     * entonces: se lanza DuplicatedEntryException.
     */
    @Test
    public void updateProductShouldThrowWhenBarCodeAlreadyExistsInOtherProduct() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.existsByNameAndIdIsNot(UPDATED_NAME, PRODUCT_ID)).thenReturn(false);
        when(productRepository.existsByCodeAndIdIsNot(UPDATED_CODE, PRODUCT_ID)).thenReturn(false);
        when(productRepository.existsByBarCodeAndIdIsNot(UPDATED_BAR_CODE, PRODUCT_ID)).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicatedEntryException.class,
                () -> productService.updateProduct(PRODUCT_ID, updateProductRequestDTO));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).existsByBarCodeAndIdIsNot(UPDATED_BAR_CODE, PRODUCT_ID);
        verify(productRepository, never()).save(any());
    }

    /**
     * dado: que existe un producto con el ID proporcionado.
     * cuando: se llama al método deleteProduct.
     * entonces: se elimina correctamente y se retorna true.
     */
    @Test
    public void deleteProductShouldDeleteSuccessfullyWhenProductExists() throws NotFoundException {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        // Act
        boolean result = productService.deleteProduct(PRODUCT_ID);

        // Assert
        assertTrue(result);
        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).delete(product);
    }

    /**
     * dado: que no existe un producto con el ID proporcionado.
     * cuando: se llama al método deleteProduct.
     * entonces: se lanza NotFoundException y no se intenta eliminar nada.
     */
    @Test
    public void deleteProductShouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.deleteProduct(PRODUCT_ID));

        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository, never()).delete((Product) any());
    }

    /**
     * dado: que existe un producto con el ID proporcionado.
     * cuando: se llama al método getProduct.
     * entonces: se retorna correctamente el producto encontrado.
     */
    @Test
    public void getProductShouldReturnProductWhenExists() throws NotFoundException {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));

        // Act
        Product result = productService.getProduct(PRODUCT_ID);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(NAME, result.getName()),
                () -> assertEquals(CODE, result.getCode()),
                () -> assertEquals(BAR_CODE, result.getBarCode()),
                () -> assertEquals(TYPE, result.getType()),
                () -> assertEquals(STATE, result.getState()));

        verify(productRepository).findById(PRODUCT_ID);
    }

    /**
     * dado: que no existe un producto con el ID proporcionado.
     * cuando: se llama al método getProduct.
     * entonces: se lanza NotFoundException.
     */
    @Test
    public void getProductShouldThrowNotFoundExceptionWhenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> productService.getProduct(PRODUCT_ID));

        verify(productRepository).findById(PRODUCT_ID);
    }

    /**
     * dado: que el parámetro specificationProductDTO es null.
     * cuando: se llama al método getProducts.
     * entonces: se retorna la lista completa de productos.
     */
    @Test
    public void getProductsShouldReturnAllWhenSpecificationIsNull() {
        // Arrange
        List<Product> mockProducts = List.of(product);
        when(productRepository.findAll()).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.getProducts(null);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(NAME, result.get(0).getName()));

        verify(productRepository).findAll();
    }

    /**
     * dado: que specificationProductDTO contiene filtros.
     * cuando: se llama al método getProducts.
     * entonces: se aplica la Specification y se retorna la lista filtrada.
     */
    @Test
    public void getProductsShouldReturnFilteredResultsWhenSpecificationProvided() {
        // Arrange
        SpecificationProductDTO dto = new SpecificationProductDTO(
                PRODUCT_ID, NAME, CODE, BAR_CODE, TYPE, STATE);
        List<Product> filteredProducts = List.of(product);

        // Nota: no testeamos las reglas internas de Specification, solo que se usa
        // correctamente
        when(productRepository.findAll(any(Specification.class))).thenReturn(filteredProducts);

        // Act
        List<Product> result = productService.getProducts(dto);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(NAME, result.get(0).getName()));

        verify(productRepository).findAll(any(Specification.class));
    }

    /**
     * dado: que se proporciona una lista de IDs válida.
     * cuando: se llama al método getProductsByIds.
     * entonces: se retorna la lista de productos correspondientes.
     */
    @Test
    public void getProductsByIdsShouldReturnProductsWhenIdsProvided() {
        // Arrange
        List<String> ids = List.of(PRODUCT_ID);
        List<Product> mockProducts = List.of(product);
        when(productRepository.findAllById(ids)).thenReturn(mockProducts);

        // Act
        List<Product> result = productService.getProductsByIds(ids);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(PRODUCT_ID, result.get(0).getId()));

        verify(productRepository).findAllById(ids);
    }

    /**
     * dado: que la lista de IDs es null.
     * cuando: se llama al método getProductsByIds.
     * entonces: se retorna una lista vacía sin llamar al repositorio.
     */
    @Test
    public void getProductsByIdsShouldReturnEmptyListWhenIdsIsNull() {
        // Act
        List<Product> result = productService.getProductsByIds(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, never()).findAllById(any());
    }

    /**
     * dado: que la lista de IDs está vacía.
     * cuando: se llama al método getProductsByIds.
     * entonces: se retorna una lista vacía sin llamar al repositorio.
     */
    @Test
    public void getProductsByIdsShouldReturnEmptyListWhenIdsIsEmpty() {
        // Act
        List<Product> result = productService.getProductsByIds(List.of());

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository, never()).findAllById(any());
    }

    /**
     * dado: que se llama al método getStates.
     * cuando: se ejecuta correctamente.
     * entonces: se retorna la lista con todos los estados definidos.
     */
    @Test
    public void getStatesShouldReturnAllEnumProductStates() {
        // Act
        List<StateProductResponseDTO> result = productService.getStates();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertTrue(result.stream().anyMatch(state -> state.getId() == EnumProductState.ACTIVE)),
                () -> assertTrue(result.stream().anyMatch(state -> state.getId() == EnumProductState.INACTIVE)),
                () -> assertTrue(result.stream().anyMatch(state -> state.getName().equals("Activo"))),
                () -> assertTrue(result.stream().anyMatch(state -> state.getName().equals("Inactivo"))));
    }

    /**
     * dado: que se llama al método getTypes.
     * cuando: se ejecuta correctamente.
     * entonces: se retorna la lista con todos los tipos definidos.
     */
    @Test
    public void getTypesShouldReturnAllEnumProductTypes() {
        // Act
        List<TypeProductResponseDTO> result = productService.getTypes();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1, result.size()),
                () -> assertEquals(EnumProductType.GOOD, result.get(0).getId()),
                () -> assertEquals("Bien", result.get(0).getName()));
    }
}