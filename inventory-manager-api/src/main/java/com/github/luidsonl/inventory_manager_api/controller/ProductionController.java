package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductionExecutionDTO;
import com.github.luidsonl.inventory_manager_api.dto.MaterialRequirementDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/requirements")
    @Operation(summary = "Get material requirements for a specific product and quantity")
    public MaterialRequirementDTO getRequirements(@RequestParam Long productId,
            @RequestParam java.math.BigDecimal quantity) {
        return productionService.calculateRequirements(productId, quantity);
    }

    @PostMapping("/execute")
    @Operation(summary = "Execute a production process, recording all transactions")
    public org.springframework.http.ResponseEntity<Void> execute(@RequestBody ProductionExecutionDTO execution) {
        productionService.executeProduction(execution);
        return org.springframework.http.ResponseEntity.ok().build();
    }
}
