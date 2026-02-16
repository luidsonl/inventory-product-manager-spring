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
public class ProductionExecutionDTO {
    private List<ProducedProductInputDTO> producedProducts;
    private List<ConsumedMaterialInputDTO> consumedMaterials;
    private String note;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProducedProductInputDTO {
        private Long productId;
        private BigDecimal quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsumedMaterialInputDTO {
        private Long packagingId;
        private Integer quantity;
    }
}
