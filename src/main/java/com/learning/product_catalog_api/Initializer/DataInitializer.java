package com.learning.product_catalog_api.Initializer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.learning.product_catalog_api.model.Product;
import com.learning.product_catalog_api.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository repository;

    @Autowired
    public DataInitializer(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            log.info("No product found in table Product, populating it....");
            Random random = new Random();
            List<Product> products = IntStream.range(1, 1000)
                    .mapToObj(i -> Product.builder()
                            .name("Test-Product" + i)
                            .description("Desc" + i)
                            .price(BigDecimal.valueOf(10 + random.nextInt(90)))
                            .available(random.nextBoolean())
                            .build())
                    .collect(Collectors.toList());
            repository.saveAll(products);
            log.info("populated db");
        }
    }
}
