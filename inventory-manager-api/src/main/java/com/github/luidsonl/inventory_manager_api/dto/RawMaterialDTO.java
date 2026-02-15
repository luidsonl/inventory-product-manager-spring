package com.github.luidsonl.inventory_manager_api.dto;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialDTO {
    private Long id;
    private String name;
    private String description;
    private MeasureUnitsType unit;
    private boolean fractionable;
}
