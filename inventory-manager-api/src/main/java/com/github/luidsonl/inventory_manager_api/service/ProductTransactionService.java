package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductTransactionDTO;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductTransaction;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import com.github.luidsonl.inventory_manager_api.repository.ProductTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTransactionService {

    private final ProductTransactionRepository transactionRepository;
    private final ProductRepository productRepository;

    public List<ProductTransactionDTO> findAll() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductTransactionDTO> findByProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        return transactionRepository.findByProduct(product).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductTransactionDTO save(ProductTransactionDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + dto.getProductId()));

        ProductTransaction transaction = new ProductTransaction();
        transaction.setProduct(product);
        transaction.setQuantity(dto.getQuantity());
        transaction.setType(dto.getType());
        transaction.setNote(dto.getNote());

        updateStock(product, transaction);

        ProductTransaction saved = transactionRepository.save(transaction);
        productRepository.save(product);

        return convertToDTO(saved);
    }

    private void updateStock(Product product, ProductTransaction transaction) {
        switch (transaction.getType()) {
            case INVENTORY_IN:
                product.setCurrentStock(product.getCurrentStock().add(transaction.getQuantity()));
                break;
            case INVENTORY_OUT:
                product.setCurrentStock(product.getCurrentStock().subtract(transaction.getQuantity()));
                break;
            case ADJUSTMENT:
                // For Adjustment, we treat quantity as a delta (positive or negative)
                product.setCurrentStock(product.getCurrentStock().add(transaction.getQuantity()));
                break;
        }
    }

    private ProductTransactionDTO convertToDTO(ProductTransaction entity) {
        return ProductTransactionDTO.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getName())
                .quantity(entity.getQuantity())
                .type(entity.getType())
                .transactionDate(entity.getTransactionDate())
                .note(entity.getNote())
                .build();
    }
}
