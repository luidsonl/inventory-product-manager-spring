package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save and retrieve a product successfully")
    void testSaveAndFindProduct() {
        Product product = new Product();
        product.setCode("P_TEST");
        product.setName("Test Product");
        product.setPrice(new BigDecimal("100.00"));
        product.setFractionable(false);
        product.setCurrentStock(BigDecimal.TEN);

        Product savedProduct = productRepository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
        assertThat(foundProduct.get().getCurrentStock()).isEqualByComparingTo(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Should find product by code")
    void testFindByCode() {
        Product product = new Product();
        product.setCode("PROD_CODE");
        product.setName("Product Name");
        product.setPrice(BigDecimal.ONE);
        product.setFractionable(false);
        productRepository.save(product);

        java.util.Optional<Product> found = productRepository.findByCode("PROD_CODE");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Product Name");
    }

    @Test
    @DisplayName("Should find products with current stock greater than specified value")
    void testFindByCurrentStockGreaterThan() {
        Product highStockProduct = new Product();
        highStockProduct.setCode("P_HIGH");
        highStockProduct.setName("High Stock Product");
        highStockProduct.setPrice(new BigDecimal("50.00"));
        highStockProduct.setFractionable(true);
        highStockProduct.setCurrentStock(new BigDecimal("20.00"));
        productRepository.save(highStockProduct);

        Product lowStockProduct = new Product();
        lowStockProduct.setCode("P_LOW");
        lowStockProduct.setName("Low Stock Product");
        lowStockProduct.setPrice(new BigDecimal("20.00"));
        lowStockProduct.setFractionable(false);
        lowStockProduct.setCurrentStock(new BigDecimal("5.00"));
        productRepository.save(lowStockProduct);

        List<Product> products = productRepository.findByCurrentStockGreaterThan(new BigDecimal("10.00"));

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo("High Stock Product");
    }
}
