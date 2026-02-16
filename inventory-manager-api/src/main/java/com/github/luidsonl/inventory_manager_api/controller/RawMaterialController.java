package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/raw-materials")
@RequiredArgsConstructor
public class RawMaterialController {

    private final RawMaterialService rawMaterialService;

    @GetMapping
    public List<RawMaterialDTO> findAll() {
        return rawMaterialService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(rawMaterialService.findById(id));
    }

    @PostMapping
    public ResponseEntity<RawMaterialDTO> save(@RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rawMaterialService.save(rawMaterialDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterialDTO> update(@PathVariable Long id, @RequestBody RawMaterialDTO rawMaterialDTO) {
        return ResponseEntity.ok(rawMaterialService.update(id, rawMaterialDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        rawMaterialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
