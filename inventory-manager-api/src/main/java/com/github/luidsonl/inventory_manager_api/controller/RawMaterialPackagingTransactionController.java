package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingTransactionDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialPackagingTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/packaging-transactions")
@RequiredArgsConstructor
@Tag(name = "Packaging Transaction", description = "Endpoints for managing stock movements of raw material packagings")
public class RawMaterialPackagingTransactionController {

    private final RawMaterialPackagingTransactionService transactionService;

    @GetMapping
    @Operation(summary = "Find all packaging transactions")
    public List<RawMaterialPackagingTransactionDTO> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/packaging/{packagingId}")
    @Operation(summary = "Find transactions for a specific packaging")
    public List<RawMaterialPackagingTransactionDTO> findByPackaging(@PathVariable Long packagingId) {
        return transactionService.findByPackaging(packagingId);
    }

    @PostMapping
    @Operation(summary = "Record a new packaging transaction and update stock")
    public ResponseEntity<RawMaterialPackagingTransactionDTO> save(
            @RequestBody RawMaterialPackagingTransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(dto));
    }
}
