package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.ProductTransactionDTO;
import com.github.luidsonl.inventory_manager_api.service.ProductTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/product-transactions")
@RequiredArgsConstructor
@Tag(name = "Product Transaction", description = "Endpoints for managing stock movements of products")
public class ProductTransactionController {

    private final ProductTransactionService transactionService;

    @GetMapping
    @Operation(summary = "Find all product transactions")
    public List<ProductTransactionDTO> findAll() {
        return transactionService.findAll();
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Find transactions for a specific product")
    public List<ProductTransactionDTO> findByProduct(@PathVariable Long productId) {
        return transactionService.findByProduct(productId);
    }

    @PostMapping
    @Operation(summary = "Record a new product transaction and update stock")
    public ResponseEntity<ProductTransactionDTO> save(@RequestBody ProductTransactionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.save(dto));
    }
}
