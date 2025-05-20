package com.learning.product_catalog_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import com.learning.product_catalog_api.data.ProductRequest;
import com.learning.product_catalog_api.data.ProductResponse;
import com.learning.product_catalog_api.model.Product;
import com.learning.product_catalog_api.repository.ProductRepository;

class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    private Map<Integer, Product> productMap;

    @BeforeEach
    void setUp() {
        setupMocks();
        setupMockData();
        setupTarget();
    }

    private void setupMockData() {
        productMap = new HashMap<>();
    }

    private void setupMocks() {
        productRepository = Mockito.mock(ProductRepository.class);
        Mockito.when(productRepository.findById(anyInt()))
                .thenAnswer((Answer<Optional<Product>>) invocationOnMock -> {
                    Integer id = (Integer) invocationOnMock.getArguments()[0];
                    return Optional.ofNullable(productMap.get(id));
                });
        Mockito.when(productRepository.existsById(anyInt()))
                .thenAnswer((Answer<Boolean>) invocationOnMock -> {
                    Integer id = invocationOnMock.getArgument(0);
                    return productMap.containsKey(id);
                });
        Mockito.when(productRepository.findAll())
                .thenAnswer((Answer<Iterable<Product>>) invocationOnMock -> productMap.values());
        Mockito.doAnswer(invocation -> {
            Integer id = invocation.getArgument(0);
            productMap.remove(id);
            return null;
        }).when(productRepository).deleteById(anyInt());
        Mockito.when(productRepository.save(isA(Product.class)))
                .thenAnswer((Answer<Product>) invocationOnMock -> {
                    Product product = (Product) invocationOnMock.getArguments()[0];
                    productMap.put(product.getId(), product);
                    return product;
                });
    }

    private void setupTarget() {
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void createProduct() {
        productMap.clear();
        ProductRequest productRequest = new ProductRequest("Test Product", "Test Description", BigDecimal.valueOf(100.0), true);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        final ProductResponse result = productService.createProduct(productRequest);
        Mockito.verify(productRepository).save(productCaptor.capture());
        final Product savedProduct = productCaptor.getValue();
        assertEquals(productRequest.name(), result.name(), "name is same");
        assertEquals(productRequest.description(), result.description(), "description is same");
        assertEquals(productRequest.price(), result.price(), "price is same");
        assertEquals(productRequest.available(), result.available(), "available is same");
        assertTrue(productMap.containsKey(savedProduct.getId()), "product is saved");
    }

    @Test
    void getProductById() {
        productMap.clear();
        Product product = new Product(1, "Test Product", "Test Description", BigDecimal.valueOf(100.0), true, null, null);
        productMap.put(product.getId(), product);
        final ProductResponse result = productService.getProductById(product.getId());
        assertEquals(product.getId(), result.id(), "id is same");
        assertEquals(product.getName(), result.name(), "name is same");
        assertEquals(product.getDescription(), result.description(), "description is same");
        assertEquals(product.getPrice(), result.price(), "price is same");
        assertEquals(product.isAvailable(), result.available(), "available is same");
    }

    @Test
    void getProductByIdNotFoundException() {
        productMap.clear();
        int productId = 1;
        try {
            productService.getProductById(productId);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Product not found: " + productId), "product not found exception");
        }
    }

    @Test
    void deleteProductById() {
        productMap.clear();
        Product product = new Product(1, "Test Product", "Test Description", BigDecimal.valueOf(100.0), true, null, null);
        productMap.put(product.getId(), product);
        productService.deleteProduct(product.getId());
        assertTrue(productMap.isEmpty(), "product is deleted");
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(product.getId());
    }

    @Test
    void deleteProductByIdNotFoundException() {
        productMap.clear();
        int productId = 1;
        try {
            productService.deleteProduct(productId);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Product not found: " + productId), "product not found exception");
        }
    }

    @Test
    void updateProduct() {
        productMap.clear();
        Product product = new Product(1, "Test Product", "Test Description", BigDecimal.valueOf(100.0), true, null, null);
        productMap.put(product.getId(), product);
        ProductRequest productRequest = new ProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(200.0), false);
        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        final ProductResponse result = productService.updateProduct(product.getId(), productRequest);
        Mockito.verify(productRepository).save(productCaptor.capture());
        final Product updatedProduct = productCaptor.getValue();
        assertEquals(productRequest.name(), result.name(), "name is same");
        assertEquals(productRequest.description(), result.description(), "description is same");
        assertEquals(productRequest.price(), result.price(), "price is same");
        assertEquals(productRequest.available(), result.available(), "available is same");
        assertTrue(productMap.containsKey(updatedProduct.getId()), "product is updated");
    }

    @Test
    void updateProductNotFoundException() {
        productMap.clear();
        int productId = 1;
        ProductRequest productRequest = new ProductRequest("Updated Product", "Updated Description", BigDecimal.valueOf(200.0), false);
        try {
            productService.updateProduct(productId, productRequest);
            fail();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("Product not found: " + productId), "product not found exception");
        }
    }
}
