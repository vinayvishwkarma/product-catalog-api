package com.learning.product_catalog_api.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ProductRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validProductRequest() {
        ProductRequest productRequest = new ProductRequest(
                "Valid Product",
                "A valid description",
                BigDecimal.valueOf(10.0),
                true);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertTrue(violations.isEmpty(), "Expected no validation errors for a valid ProductRequest");
    }

    @Test
    void invalidProductNameForSingleCharLength() {
        ProductRequest productRequest = new ProductRequest(
                "1",
                "A valid description",
                BigDecimal.valueOf(10.0),
                true);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        String error = violations.iterator().next().getMessage();
        assertTrue(error.contains("Product name must be between 2 and 100 characters"), "Product name must be between 2 and 100 characters");
    }

    @Test
    void invalidProductNameBlankString() {
        ProductRequest productRequest = new ProductRequest(
                "",
                "A valid description",
                BigDecimal.valueOf(10.0),
                true);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(2, violations.size());
        Set<String> errorMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
        assertTrue(errorMessages.contains("Product name must be between 2 and 100 characters"),
                "Expected error for name length");
        assertTrue(errorMessages.contains("Product name is required"),
                "Expected error for blank name");
    }

    @Test
    void invalidPrice() {
        ProductRequest productRequest = new ProductRequest(
                "Valid Product",
                "A valid description",
                BigDecimal.valueOf(-1.0),
                true);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("Price must be greater than 0", violations.iterator().next().getMessage());
    }

    @Test
    void nullPrice() {
        ProductRequest productRequest = new ProductRequest(
                "Valid Product",
                "A valid description",
                null,
                true);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertEquals(1, violations.size());
        assertEquals("Price is required", violations.iterator().next().getMessage());
    }

    @Test
    void invalidAvailability() {
        ProductRequest productRequest = new ProductRequest(
                "Valid Product",
                "A valid description",
                BigDecimal.valueOf(10.0),
                false);
        Set<ConstraintViolation<ProductRequest>> violations = validator.validate(productRequest);
        assertTrue(violations.isEmpty(), "Expected no validation errors for availability being false");
    }
}
