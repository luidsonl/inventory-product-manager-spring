package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Endpoints for managing products and their raw material associations")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "Find all products")
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find a product by its ID")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing product")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a product by its ID")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/raw-materials")
    @Operation(summary = "Associate a raw material with a product")
    public ResponseEntity<ProductRawMaterialDTO> addRawMaterial(
            @PathVariable Long id,
            @RequestParam Long rawMaterialId,
            @RequestParam BigDecimal quantity) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addRawMaterial(id, rawMaterialId, quantity));
    }

    @DeleteMapping("/raw-materials/{associationId}")
    @Operation(summary = "Remove a raw material association from a product")
    public ResponseEntity<Void> removeRawMaterial(@PathVariable Long associationId) {
        productService.removeRawMaterial(associationId);
        return ResponseEntity.noContent().build();
    }
}
