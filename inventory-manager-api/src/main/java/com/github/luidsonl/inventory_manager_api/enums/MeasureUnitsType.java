package com.github.luidsonl.inventory_manager_api.enums;

import lombok.Getter;

@Getter
public enum MeasureUnitsType {
    
    MILLIGRAM("mg"),
    GRAM("g"),
    KILOGRAM("kg"),
    TON("t"),

    MILLILITER("ml"),
    LITER("l"),

    UNIT("un"),
    PIECE("pc"),
    BOX("bx"),
    PACK("pk"),
    DOZEN("dz");

    private final String symbol;

    MeasureUnitsType(String symbol) {
        this.symbol = symbol;
    }
}