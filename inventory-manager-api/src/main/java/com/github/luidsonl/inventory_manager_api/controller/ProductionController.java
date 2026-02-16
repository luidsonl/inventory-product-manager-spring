package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@Tag(name = "Production", description = "Endpoints for production suggestions and analysis")
public class ProductionController {

    private final ProductionService productionService;

    @GetMapping("/suggest")
    @Operation(summary = "Get production suggestions based on current stock levels")
    public ProductionSuggestionDTO suggest() {
        return productionService.getProductionSuggestion();
    }
}
