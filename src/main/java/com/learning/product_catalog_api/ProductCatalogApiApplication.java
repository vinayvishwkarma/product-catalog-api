package com.learning.product_catalog_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableCaching
@EnableSpringDataWebSupport
public class ProductCatalogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApiApplication.class, args);
    }
}
