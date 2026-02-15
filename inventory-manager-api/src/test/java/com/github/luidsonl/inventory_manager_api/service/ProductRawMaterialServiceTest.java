package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRawMaterialRepository;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRawMaterialServiceTest {

    @Mock
    private ProductRawMaterialRepository productRawMaterialRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductRawMaterialService productRawMaterialService;

    @Test
    @DisplayName("Should return all associations")
    void testFindAll() {
        ProductRawMaterial association = new ProductRawMaterial();
        RawMaterial material = new RawMaterial();
        material.setId(1L);
        association.setRawMaterial(material);

        when(productRawMaterialRepository.findAll()).thenReturn(Collections.singletonList(association));

        List<ProductRawMaterialDTO> result = productRawMaterialService.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Should save association using IDs")
    void testSave() {
        Product product = new Product();
        product.setId(1L);
        RawMaterial material = new RawMaterial();
        material.setId(2L);
        material.setName("Sugar");

        ProductRawMaterial saved = new ProductRawMaterial();
        saved.setId(10L);
        saved.setProduct(product);
        saved.setRawMaterial(material);
        saved.setQuantityNeeded(new BigDecimal("2.5"));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(2L)).thenReturn(Optional.of(material));
        when(productRawMaterialRepository.save(any(ProductRawMaterial.class))).thenReturn(saved);

        ProductRawMaterialDTO dto = ProductRawMaterialDTO.builder().quantityNeeded(new BigDecimal("2.5")).build();
        ProductRawMaterialDTO result = productRawMaterialService.save(1L, 2L, dto);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getRawMaterialName()).isEqualTo("Sugar");
    }

    @Test
    @DisplayName("Should update association quantity")
    void testUpdate() {
        ProductRawMaterial existing = new ProductRawMaterial();
        existing.setId(1L);
        RawMaterial material = new RawMaterial();
        material.setId(2L);
        existing.setRawMaterial(material);

        ProductRawMaterialDTO updatedInfo = ProductRawMaterialDTO.builder().quantityNeeded(new BigDecimal("10.0"))
                .build();

        when(productRawMaterialRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRawMaterialRepository.save(any(ProductRawMaterial.class))).thenReturn(existing);

        ProductRawMaterialDTO result = productRawMaterialService.update(1L, updatedInfo);

        assertThat(result.getQuantityNeeded()).isEqualTo(new BigDecimal("10.0"));
    }

    @Test
    @DisplayName("Should delete association by id")
    void testDeleteById() {
        productRawMaterialService.deleteById(1L);
        verify(productRawMaterialRepository, times(1)).deleteById(1L);
    }
}
