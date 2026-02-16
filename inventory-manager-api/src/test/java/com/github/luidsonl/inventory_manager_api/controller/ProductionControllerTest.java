package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.dto.SuggestedProductDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
