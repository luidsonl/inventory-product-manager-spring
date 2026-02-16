package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
@Tag(name = "Raw Material", description = "Endpoints for managing inventory raw materials")
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    @Operation(summary = "Find all raw materials")
    public List<RawMaterialDTO> findAll() {
        return rawMaterialService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a raw material by its ID")
    public ResponseEntity<RawMaterialDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new raw material")
    public ResponseEntity<RawMaterialDTO> save(@RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.save(rawMaterialDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing raw material")
    public ResponseEntity<RawMaterialDTO> update(@PathVariable Long id, @RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.ok(rawMaterialService.update(id, rawMaterialDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a raw material by its ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rawMaterialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
