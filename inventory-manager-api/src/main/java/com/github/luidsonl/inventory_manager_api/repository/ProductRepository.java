package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCurrentStockGreaterThan(java.math.BigDecimal stock);
}
