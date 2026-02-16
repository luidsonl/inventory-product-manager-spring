package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRawMaterialRepositoryTest {

    @Autowired
    private ProductRawMaterialRepository productRawMaterialRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Test
    @DisplayName("Should find ProductRawMaterial by Product")
    void testFindByProduct() {
        Product product = new Product();
        product.setCode("PA");
        product.setName("Product A");
        product.setPrice(BigDecimal.ONE);
        productRepository.save(product);

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("MA");
        rawMaterial.setName("Material A");
        rawMaterial.setUnit(MeasureUnitsType.KILOGRAM);
        rawMaterialRepository.save(rawMaterial);

        ProductRawMaterial association = new ProductRawMaterial();
        association.setProduct(product);
        association.setRawMaterial(rawMaterial);
        association.setQuantityNeeded(new BigDecimal("2.5"));
        productRawMaterialRepository.save(association);

        List<ProductRawMaterial> results = productRawMaterialRepository.findByProduct(product);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getRawMaterial().getName()).isEqualTo("Material A");
    }

    @Test
    @DisplayName("Should find ProductRawMaterial by RawMaterial")
    void testFindByRawMaterial() {
        Product product = new Product();
        product.setCode("PB");
        product.setName("Product B");
        product.setPrice(BigDecimal.TEN);
        productRepository.save(product);

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("MB");
        rawMaterial.setName("Material B");
        rawMaterial.setUnit(MeasureUnitsType.LITER);
        rawMaterialRepository.save(rawMaterial);

        ProductRawMaterial association = new ProductRawMaterial();
        association.setProduct(product);
        association.setRawMaterial(rawMaterial);
        association.setQuantityNeeded(new BigDecimal("5.0"));
        productRawMaterialRepository.save(association);

        List<ProductRawMaterial> results = productRawMaterialRepository.findByRawMaterial(rawMaterial);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getProduct().getName()).isEqualTo("Product B");
    }
}
