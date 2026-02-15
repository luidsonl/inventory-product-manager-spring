package com.github.luidsonl.inventory_manager_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionDTO {
    private List<SuggestedProductDTO> products;
    private BigDecimal grandTotalValue;
}
