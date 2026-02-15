package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.ProductTransaction;
import com.github.luidsonl.inventory_manager_api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTransactionRepository extends JpaRepository<ProductTransaction, Long> {
    List<ProductTransaction> findByProduct(Product product);
}
