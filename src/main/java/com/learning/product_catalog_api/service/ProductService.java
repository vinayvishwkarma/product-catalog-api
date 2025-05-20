package com.learning.product_catalog_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.learning.product_catalog_api.data.ProductRequest;
import com.learning.product_catalog_api.data.ProductResponse;

public interface ProductService {

    /**
     * Create a new product.
     *
     * @param product the product to create
     * @return the created product
     */
    ProductResponse createProduct(ProductRequest product);

    /**
     * Get a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the product with the specified ID
     */
    ProductResponse getProductById(int id);

    /**
     * Get all products.
     *
     * @return a list of all products
     */
    Page<ProductResponse> getAllProducts(Pageable pageable);

    /**
     * Update an existing product.
     *
     * @param id      the ID of the product to update
     * @param product the updated product data
     * @return the updated product
     */
    ProductResponse updateProduct(int id, ProductRequest product);

    /**
     * Delete a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    void deleteProduct(int id);
}
