package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> findAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/raw-materials")
    public ResponseEntity<ProductRawMaterialDTO> addRawMaterial(
            @PathVariable Long id,
            @RequestParam Long rawMaterialId,
            @RequestParam BigDecimal quantity) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.addRawMaterial(id, rawMaterialId, quantity));
    }

    @DeleteMapping("/raw-materials/{associationId}")
    public ResponseEntity<Void> removeRawMaterial(@PathVariable Long associationId) {
        productService.removeRawMaterial(associationId);
        return ResponseEntity.noContent().build();
    }
}
