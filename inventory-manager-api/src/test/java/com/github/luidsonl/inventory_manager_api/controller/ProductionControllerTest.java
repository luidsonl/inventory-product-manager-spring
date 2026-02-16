package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.MaterialRequirementDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductionExecutionDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.dto.SuggestedProductDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductionController.class)
class ProductionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductionService productionService;

    @Test
    @DisplayName("Should return production suggestions with items and totals")
    void testSuggest() throws Exception {
        SuggestedProductDTO item = new SuggestedProductDTO(1L, "Test Product", new BigDecimal("10.0"),
                new BigDecimal("50.0"), new BigDecimal("500.0"));
        ProductionSuggestionDTO dto = new ProductionSuggestionDTO(Collections.singletonList(item),
                new BigDecimal("500.0"));

        when(productionService.getProductionSuggestion()).thenReturn(dto);

        mockMvc.perform(get("/api/production/suggest"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "products": [
                                {
                                    "productName": "Test Product",
                                    "quantityToProduce": 10.0
                                }
                            ],
                            "grandTotalValue": 500.0
                        }
                        """));

        verify(productionService).getProductionSuggestion();
    }

    @Test
    @DisplayName("Should return requirements for a product")
    void testGetRequirements() throws Exception {
        MaterialRequirementDTO dto = MaterialRequirementDTO.builder()
                .productId(1L)
                .productName("Test Product")
                .productionQuantity(new BigDecimal("5.0"))
                .materials(List.of(
                        MaterialRequirementDTO.MaterialItemDTO.builder()
                                .rawMaterialId(2L)
                                .rawMaterialName("Sugar")
                                .totalNeededQuantity(new BigDecimal("2.5"))
                                .unit("KILOGRAM")
                                .build()))
                .build();

        when(productionService.calculateRequirements(eq(1L), any(BigDecimal.class))).thenReturn(dto);

        mockMvc.perform(get("/api/production/requirements")
                .param("productId", "1")
                .param("quantity", "5.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Test Product"))
                .andExpect(jsonPath("$.materials[0].rawMaterialName").value("Sugar"));
    }

    @Test
    @DisplayName("Should execute production")
    void testExecute() throws Exception {
        ProductionExecutionDTO execution = ProductionExecutionDTO.builder()
                .note("Test production")
                .producedProducts(List.of(new ProductionExecutionDTO.ProducedProductInputDTO(1L, new BigDecimal("10"))))
                .consumedMaterials(List.of(new ProductionExecutionDTO.ConsumedMaterialInputDTO(1L, 5)))
                .build();

        mockMvc.perform(post("/api/production/execute")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(execution)))
                .andExpect(status().isOk());

        verify(productionService).executeProduction(any(ProductionExecutionDTO.class));
    }
}
