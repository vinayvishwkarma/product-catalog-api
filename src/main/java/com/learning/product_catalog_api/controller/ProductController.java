package com.learning.product_catalog_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.learning.product_catalog_api.data.ProductRequest;
import com.learning.product_catalog_api.data.ProductResponse;
import com.learning.product_catalog_api.service.ProductService;

import jakarta.validation.Valid;

/**
 * ProductController handles HTTP requests related to product operations.
 * It provides endpoints for creating, retrieving, updating, and deleting products.
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to create a new product.
     *
     * @param product the product request object containing product details.
     * @return ResponseEntity containing the created ProductResponse object.
     */
    @PostMapping(value = "/create", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {
        ProductResponse createdProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    /**
     * Endpoint to get a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return ResponseEntity containing the ProductResponse object if found, or 404 Not Found if not found.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable int id) {
        ProductResponse product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }

    /**
     * Endpoint to list all products with pagination.
     *
     * @param page the page number to retrieve.
     * @param size the number of products per page.
     * @return ResponseEntity containing a PagedModel of ProductResponse objects.
     */
    @GetMapping(value = "/list", produces = "application/json")
    public ResponseEntity<PagedModel<EntityModel<ProductResponse>>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<ProductResponse> pagedResourcesAssembler) {
        if (pagedResourcesAssembler == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
        Page<ProductResponse> products = productService.getAllProducts(PageRequest.of(page, size));
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        PagedModel<EntityModel<ProductResponse>> pagedModel = pagedResourcesAssembler.toModel(
                products, product -> EntityModel.of(product));
        return ResponseEntity.ok(pagedModel);
    }

    /**
     * Endpoint to update an existing product.
     *
     * @param id      the ID of the product to update.
     * @param product the product request object containing updated product details.
     * @return ResponseEntity containing the updated ProductResponse object.
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequest product) {
        ProductResponse updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Endpoint to delete a product by its ID.
     *
     * @param id the ID of the product to delete.
     * @return ResponseEntity with no content if successful, or 404 Not Found if not found.
     */
    @DeleteMapping(value = "/{id}")
    private ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
