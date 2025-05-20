package com.learning.product_catalog_api.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ProductResponse is a record that represents the response object for product-related operations.
 * It contains fields such as id, name, description, price, availability status, and timestamps for creation and update.
 */
public record ProductResponse(
        int id,
        String name,
        String description,
        BigDecimal price,
        boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) implements Serializable {
}
