package com.learning.product_catalog_api.util;

import com.learning.product_catalog_api.data.ProductRequest;
import com.learning.product_catalog_api.data.ProductResponse;
import com.learning.product_catalog_api.model.Product;

public class ProductMapper {

    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable(),
                product.getCreatedAt(),
                product.getUpdatedAt());
    }

    public static Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .available(productRequest.available())
                .build();
    }
}
