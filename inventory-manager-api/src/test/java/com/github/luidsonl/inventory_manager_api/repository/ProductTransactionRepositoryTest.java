package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductTransactionRepositoryTest {

    @Autowired
    private ProductTransactionRepository transactionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should find transactions by Product")
    void testFindByProduct() {
        Product product = new Product();
        product.setName("Sold Product");
        product.setPrice(new BigDecimal("150.00"));
        productRepository.save(product);

        ProductTransaction transaction = new ProductTransaction();
        transaction.setProduct(product);
        transaction.setType(TransactionType.INVENTORY_OUT);
        transaction.setQuantity(new BigDecimal("2.0"));
        transaction.setNote("Sale #123");
        transactionRepository.save(transaction);

        List<ProductTransaction> transactions = transactionRepository.findByProduct(product);

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getType()).isEqualTo(TransactionType.INVENTORY_OUT);
        assertThat(transactions.get(0).getTransactionDate()).isNotNull(); // Expect pre-persist to work
    }
}
