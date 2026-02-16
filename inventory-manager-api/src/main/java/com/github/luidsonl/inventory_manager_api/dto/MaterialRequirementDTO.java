package com.github.luidsonl.inventory_manager_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequirementDTO {
    private Long productId;
    private String productName;
    private BigDecimal productionQuantity;
    private List<MaterialItemDTO> materials;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MaterialItemDTO {
        private Long rawMaterialId;
        private String rawMaterialName;
        private BigDecimal totalNeededQuantity;
        private String unit;
    }
}
