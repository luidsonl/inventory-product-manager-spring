package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("Should return list of products with correct data mapping")
    void testFindAll() throws Exception {
        ProductDTO p1 = ProductDTO.builder().id(1L).code("P001").name("Product 1").price(new BigDecimal("10.50"))
                .fractionable(false)
                .build();
        ProductDTO p2 = ProductDTO.builder().id(2L).code("P002").name("Product 2").price(new BigDecimal("20.00"))
                .fractionable(true)
                .build();

        when(productService.findAll()).thenReturn(Arrays.asList(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {"id": 1, "code": "P001", "name": "Product 1", "price": 10.50, "fractionable": false},
                            {"id": 2, "code": "P002", "name": "Product 2", "price": 20.00, "fractionable": true}
                        ]
                        """));

        verify(productService).findAll();
    }

    @Test
    @DisplayName("Should return product by id")
    void testFindById() throws Exception {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .code("P_TEST")
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .fractionable(false)
                .build();
        when(productService.findById(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": 1,
                            "code": "P_TEST",
                            "name": "Test Product",
                            "price": 100.00,
                            "fractionable": false
                        }
                        """));

        verify(productService).findById(1L);
    }

    @Test
    @DisplayName("Should create product and verify input mapping")
    void testSave() throws Exception {
        ProductDTO savedDTO = ProductDTO.builder().id(123L).code("NEW_P").name("New Product")
                .price(new BigDecimal("50.0"))
                .fractionable(true).build();
        when(productService.save(any(ProductDTO.class))).thenReturn(savedDTO);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"NEW_P\", \"name\":\"New Product\", \"price\": 50.0, \"fractionable\": true}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 123,
                            "code": "NEW_P",
                            "name": "New Product",
                            "price": 50.0,
                            "fractionable": true
                        }
                        """));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).save(captor.capture());

        ProductDTO captured = captor.getValue();
        assertThat(captured.getCode()).isEqualTo("NEW_P");
        assertThat(captured.getName()).isEqualTo("New Product");
        assertThat(captured.getPrice()).isEqualByComparingTo("50.0");
        assertThat(captured.isFractionable()).isTrue();
    }

    @Test
    @DisplayName("Should update product and verify path variable and body mapping")
    void testUpdate() throws Exception {
        ProductDTO responseDTO = ProductDTO.builder().id(1L).code("UPDATED").name("Updated Name")
                .price(new BigDecimal("99.99"))
                .build();
        when(productService.update(eq(1L), any(ProductDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"code\": \"UPDATED\", \"name\":\"Updated Name\", \"price\": 99.99, \"fractionable\": false}"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "code": "UPDATED",
                            "name": "Updated Name",
                            "price": 99.99
                        }
                        """));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).update(eq(1L), captor.capture());

        ProductDTO captured = captor.getValue();
        assertThat(captured.getCode()).isEqualTo("UPDATED");
        assertThat(captured.getName()).isEqualTo("Updated Name");
        assertThat(captured.getPrice()).isEqualByComparingTo("99.99");
    }

    @Test
    @DisplayName("Should delete product by id")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService).deleteById(1L);
    }

    @Test
    @DisplayName("Should add raw material association with correct parameters")
    void testAddRawMaterial() throws Exception {
        ProductRawMaterialDTO association = ProductRawMaterialDTO.builder()
                .id(10L)
                .rawMaterialName("Water")
                .quantityNeeded(new BigDecimal("5.0"))
                .build();

        when(productService.addRawMaterial(eq(1L), eq(2L), any(BigDecimal.class))).thenReturn(association);

        mockMvc.perform(post("/api/products/1/raw-materials")
                .param("rawMaterialId", "2")
                .param("quantity", "5.0"))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                            "id": 10,
                            "rawMaterialName": "Water",
                            "quantityNeeded": 5.0
                        }
                        """));

        verify(productService).addRawMaterial(eq(1L), eq(2L), eq(new BigDecimal("5.0")));
    }

    @Test
    @DisplayName("Should remove raw material association")
    void testRemoveRawMaterial() throws Exception {
        mockMvc.perform(delete("/api/products/raw-materials/10"))
                .andExpect(status().isNoContent());

        verify(productService).removeRawMaterial(10L);
    }

    @Test
    @DisplayName("Should return 404 when product not found")
    void testFindByIdNotFound() throws Exception {
        when(productService.findById(99L)).thenThrow(new ResourceNotFoundException("Product not found with id: 99"));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found with id: 99"));

        verify(productService).findById(99L);
    }
}
