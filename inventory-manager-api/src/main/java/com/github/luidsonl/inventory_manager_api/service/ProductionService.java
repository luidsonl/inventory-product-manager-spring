package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.*;
import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionService {

    private final ProductRepository productRepository;
    private final ProductTransactionService productTransactionService;
    private final RawMaterialPackagingTransactionService packagingTransactionService;

    public ProductionSuggestionDTO getProductionSuggestion() {
        List<Product> products = productRepository.findAll();

        List<Product> sortedProducts = products.stream()
                .sorted((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()))
                .collect(Collectors.toList());

        Map<Long, BigDecimal> availableRawMaterials = new HashMap<>();

        sortedProducts.forEach(product -> {
            if (product.getRawMaterials() != null) {
                product.getRawMaterials().forEach(prm -> {
                    RawMaterial rm = prm.getRawMaterial();
                    if (!availableRawMaterials.containsKey(rm.getId())) {
                        availableRawMaterials.put(rm.getId(), calculateTotalMaterialStock(rm));
                    }
                });
            }
        });

        List<SuggestedProductDTO> suggestions = new ArrayList<>();
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (Product product : sortedProducts) {
            if (product.getRawMaterials() == null || product.getRawMaterials().isEmpty()) {
                continue;
            }

            BigDecimal maxProducible = null;

            for (ProductRawMaterial prm : product.getRawMaterials()) {
                BigDecimal available = availableRawMaterials.get(prm.getRawMaterial().getId());
                BigDecimal neededPerUnit = prm.getQuantityNeeded();

                if (neededPerUnit == null || neededPerUnit.compareTo(BigDecimal.ZERO) == 0)
                    continue;

                BigDecimal producibleForThisMaterial = available.divide(neededPerUnit, 4, RoundingMode.FLOOR);

                if (maxProducible == null || producibleForThisMaterial.compareTo(maxProducible) < 0) {
                    maxProducible = producibleForThisMaterial;
                }
            }

            if (maxProducible != null && maxProducible.compareTo(BigDecimal.ZERO) > 0) {
                if (!product.isFractionable()) {
                    maxProducible = maxProducible.setScale(0, RoundingMode.FLOOR);
                }

                if (maxProducible.compareTo(BigDecimal.ZERO) > 0) {
                    for (ProductRawMaterial prm : product.getRawMaterials()) {
                        BigDecimal consumed = maxProducible.multiply(prm.getQuantityNeeded());
                        BigDecimal remaining = availableRawMaterials.get(prm.getRawMaterial().getId())
                                .subtract(consumed);
                        availableRawMaterials.put(prm.getRawMaterial().getId(), remaining);
                    }

                    BigDecimal totalPrice = maxProducible.multiply(product.getPrice());
                    suggestions.add(new SuggestedProductDTO(
                            product.getId(),
                            product.getName(),
                            maxProducible,
                            product.getPrice(),
                            totalPrice));
                    grandTotal = grandTotal.add(totalPrice);
                }
            }
        }

        return new ProductionSuggestionDTO(suggestions, grandTotal);
    }

    private BigDecimal calculateTotalMaterialStock(RawMaterial rm) {
        BigDecimal totalStock = BigDecimal.ZERO;
        if (rm.getPackagings() != null) {
            for (var packaging : rm.getPackagings()) {
                BigDecimal packagingStock = packaging.getQuantityInside()
                        .multiply(new BigDecimal(packaging.getCurrentStock()));
                totalStock = totalStock.add(packagingStock);
            }
        }
        return totalStock;
    }

    public MaterialRequirementDTO calculateRequirements(Long productId, BigDecimal quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<MaterialRequirementDTO.MaterialItemDTO> materials = product.getRawMaterials().stream()
                .map(prm -> MaterialRequirementDTO.MaterialItemDTO.builder()
                        .rawMaterialId(prm.getRawMaterial().getId())
                        .rawMaterialName(prm.getRawMaterial().getName())
                        .totalNeededQuantity(quantity.multiply(prm.getQuantityNeeded()))
                        .unit(prm.getRawMaterial().getUnit().name())
                        .build())
                .collect(Collectors.toList());

        return MaterialRequirementDTO.builder()
                .productId(productId)
                .productName(product.getName())
                .productionQuantity(quantity)
                .materials(materials)
                .build();
    }

    @Transactional
    public void executeProduction(ProductionExecutionDTO execution) {
        // Record Produces
        if (execution.getProducedProducts() != null) {
            execution.getProducedProducts().forEach(p -> {
                productTransactionService.save(ProductTransactionDTO.builder()
                        .productId(p.getProductId())
                        .quantity(p.getQuantity())
                        .type(TransactionType.INVENTORY_IN)
                        .note("Production: " + execution.getNote())
                        .build());
            });
        }

        // Record Consumptions
        if (execution.getConsumedMaterials() != null) {
            execution.getConsumedMaterials().forEach(m -> {
                packagingTransactionService.save(RawMaterialPackagingTransactionDTO.builder()
                        .packagingId(m.getPackagingId())
                        .quantity(m.getQuantity())
                        .type(TransactionType.INVENTORY_OUT)
                        .note("Production Consumption: " + execution.getNote())
                        .build());
            });
        }
    }
}
