package com.learning.product_catalog_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.product_catalog_api.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {}
