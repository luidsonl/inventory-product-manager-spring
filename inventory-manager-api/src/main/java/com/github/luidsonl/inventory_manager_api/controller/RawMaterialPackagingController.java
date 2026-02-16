package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialPackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/packaging")
@RequiredArgsConstructor
@Tag(name = "Packaging", description = "Endpoints for managing raw material packaging and stock")
public class RawMaterialPackagingController {

    private final RawMaterialPackagingService packagingService;

    @GetMapping
    @Operation(summary = "Find all packaging items")
    public List<RawMaterialPackagingDTO> findAll() {
        return packagingService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a packaging item by its ID")
    public ResponseEntity<RawMaterialPackagingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(packagingService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new packaging item")
    public ResponseEntity<RawMaterialPackagingDTO> save(@RequestBody RawMaterialPackagingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packagingService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing packaging item")
    public ResponseEntity<RawMaterialPackagingDTO> update(@PathVariable Long id,
            @RequestBody RawMaterialPackagingDTO dto) {
        return ResponseEntity.ok(packagingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a packaging item by its ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        packagingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/raw-material/{rawMaterialId}")
    @Operation(summary = "Find all packaging items associated with a specific raw material")
    public List<RawMaterialPackagingDTO> findByRawMaterialId(@PathVariable Long rawMaterialId) {
        return packagingService.findByRawMaterialId(rawMaterialId);
    }
}
