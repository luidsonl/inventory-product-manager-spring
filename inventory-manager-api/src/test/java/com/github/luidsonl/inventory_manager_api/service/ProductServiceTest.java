package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductDTO;
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
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductRawMaterialRepository productRawMaterialRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should return all products as DTOs")
    void testFindAll() {
        Product product = new Product();
        product.setCode("P001");
        product.setName("Test Product");
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductDTO> result = productService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("P001");
        assertThat(result.get(0).getName()).isEqualTo("Test Product");
    }

    @Test
    @DisplayName("Should find product by id and return DTO")
    void testFindById() {
        Product product = new Product();
        product.setId(1L);
        product.setCode("P001");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCode()).isEqualTo("P001");
    }

    @Test
    @DisplayName("Should find product by code and return DTO")
    void testFindByCode() {
        Product product = new Product();
        product.setId(1L);
        product.setCode("P001");
        when(productRepository.findByCode("P001")).thenReturn(Optional.of(product));

        ProductDTO result = productService.findByCode("P001");

        assertThat(result.getCode()).isEqualTo("P001");
    }

    @Test
    @DisplayName("Should save a product from DTO")
    void testSave() {
        Product product = new Product();
        product.setCode("P001");
        product.setName("New Product");
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO dto = ProductDTO.builder().code("P001").name("New Product").build();
        ProductDTO result = productService.save(dto);

        assertThat(result.getName()).isEqualTo("New Product");
        assertThat(result.getCode()).isEqualTo("P001");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should update a product using DTO")
    void testUpdate() {
        Product existing = new Product();
        existing.setId(1L);
        existing.setName("Old Name");

        ProductDTO updatedDTO = ProductDTO.builder().code("P_NEW").name("New Name").build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));

        Product updated = new Product();
        updated.setId(1L);
        updated.setCode("P_NEW");
        updated.setName("New Name");
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        ProductDTO result = productService.update(1L, updatedDTO);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getCode()).isEqualTo("P_NEW");
        verify(productRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should add raw material association and return DTO")
    void testAddRawMaterial() {
        Product product = new Product();
        product.setId(1L);
        RawMaterial material = new RawMaterial();
        material.setId(2L);
        material.setName("Water");
        BigDecimal quantity = new BigDecimal("5.0");

        ProductRawMaterial association = new ProductRawMaterial();
        association.setId(3L);
        association.setProduct(product);
        association.setRawMaterial(material);
        association.setQuantityNeeded(quantity);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(2L)).thenReturn(Optional.of(material));
        when(productRawMaterialRepository.save(any(ProductRawMaterial.class))).thenReturn(association);

        ProductRawMaterialDTO result = productService.addRawMaterial(1L, 2L, quantity);

        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getRawMaterialName()).isEqualTo("Water");
        verify(productRawMaterialRepository, times(1)).save(any(ProductRawMaterial.class));
    }

    @Test
    @DisplayName("Should remove raw material association")
    void testRemoveRawMaterial() {
        productService.removeRawMaterial(1L);
        verify(productRawMaterialRepository, times(1)).deleteById(1L);
    }
}
