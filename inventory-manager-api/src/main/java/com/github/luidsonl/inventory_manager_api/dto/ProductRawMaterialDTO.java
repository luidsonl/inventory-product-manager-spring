package com.github.luidsonl.inventory_manager_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRawMaterialDTO {
    private Long id;
    private Long rawMaterialId;
    private String rawMaterialName;
    private BigDecimal quantityNeeded;
}
