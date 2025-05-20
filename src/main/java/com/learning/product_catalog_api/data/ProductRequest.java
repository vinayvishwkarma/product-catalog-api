package com.learning.product_catalog_api.data;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * ProductRequest is a record that represents the request object for product-related operations.
 * It contains fields such as name, description, price, and availability status.
 */
public record ProductRequest(
        @NotBlank(message = "Product name is required") @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters") String name,
        String description,
        @NotNull(message = "Price is required") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal price,
        @NotNull(message = "Stock availability status is required ") boolean available) implements Serializable {
}
