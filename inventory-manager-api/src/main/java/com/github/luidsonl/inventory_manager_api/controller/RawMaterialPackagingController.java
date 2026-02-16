package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialPackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packaging")
@RequiredArgsConstructor
public class RawMaterialPackagingController {

    private final RawMaterialPackagingService packagingService;

    @GetMapping
    public List<RawMaterialPackagingDTO> findAll() {
        return packagingService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialPackagingDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(packagingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RawMaterialPackagingDTO> save(@RequestBody RawMaterialPackagingDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(packagingService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialPackagingDTO> update(@PathVariable Long id,
            @RequestBody RawMaterialPackagingDTO dto) {
        return ResponseEntity.ok(packagingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        packagingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/raw-material/{rawMaterialId}")
    public List<RawMaterialPackagingDTO> findByRawMaterialId(@PathVariable Long rawMaterialId) {
        return packagingService.findByRawMaterialId(rawMaterialId);
    }
}
