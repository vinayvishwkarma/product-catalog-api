package com.learning.product_catalog_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.learning.product_catalog_api.data.ProductRequest;
import com.learning.product_catalog_api.data.ProductResponse;
import com.learning.product_catalog_api.exception.ProductNotFoundException;
import com.learning.product_catalog_api.model.Product;
import com.learning.product_catalog_api.repository.ProductRepository;
import com.learning.product_catalog_api.util.ProductMapper;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse createProduct(@NotNull final ProductRequest product) {
        Product newProduct = ProductMapper.toProduct(product);
        productRepository.save(newProduct);
        return ProductMapper.toProductResponse(newProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(final int id) {
        return productRepository.findById(id)
                .map(ProductMapper::toProductResponse)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(final Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toProductResponse);
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(final int id, @Nonnull final ProductRequest product) {
        final Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
        existingProduct.setName(product.name());
        existingProduct.setDescription(product.description());
        existingProduct.setPrice(product.price());
        existingProduct.setAvailable(product.available());
        productRepository.save(existingProduct);
        return ProductMapper.toProductResponse(existingProduct);
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(final int id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
