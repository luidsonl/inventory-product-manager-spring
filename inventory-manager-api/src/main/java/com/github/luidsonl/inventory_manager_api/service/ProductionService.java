package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.dto.SuggestedProductDTO;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public ProductionSuggestionDTO suggestProduction() {
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
                        BigDecimal totalStock = BigDecimal.ZERO;
                        if (rm.getPackagings() != null) {
                            for (var packaging : rm.getPackagings()) {
                                BigDecimal packagingStock = packaging.getQuantityInside()
                                        .multiply(new BigDecimal(packaging.getCurrentStock()));
                                totalStock = totalStock.add(packagingStock);
                            }
                        }
                        availableRawMaterials.put(rm.getId(), totalStock);
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

            // Calculate max quantity of this product that can be produced
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
}
