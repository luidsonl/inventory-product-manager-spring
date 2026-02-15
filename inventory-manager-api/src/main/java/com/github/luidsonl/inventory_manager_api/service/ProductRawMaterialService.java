package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRawMaterialRepository;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductRawMaterialService {

    private final ProductRawMaterialRepository productRawMaterialRepository;
    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public List<ProductRawMaterialDTO> findAll() {
        return productRawMaterialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductRawMaterialDTO findById(Long id) {
        ProductRawMaterial association = productRawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Association not found with id: " + id));
        return convertToDTO(association);
    }

    @Transactional
    public ProductRawMaterialDTO save(Long productId, Long rawMaterialId, ProductRawMaterialDTO dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                .orElseThrow(() -> new RuntimeException("Raw Material not found with id: " + rawMaterialId));

        ProductRawMaterial association = new ProductRawMaterial();
        association.setProduct(product);
        association.setRawMaterial(rawMaterial);
        association.setQuantityNeeded(dto.getQuantityNeeded());

        ProductRawMaterial saved = productRawMaterialRepository.save(association);
        return convertToDTO(saved);
    }

    @Transactional
    public ProductRawMaterialDTO update(Long id, ProductRawMaterialDTO dto) {
        ProductRawMaterial existing = productRawMaterialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Association not found with id: " + id));

        existing.setQuantityNeeded(dto.getQuantityNeeded());

        ProductRawMaterial saved = productRawMaterialRepository.save(existing);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        productRawMaterialRepository.deleteById(id);
    }

    public List<ProductRawMaterialDTO> findByProductId(Long productId) {
        return productRawMaterialRepository.findAll().stream()
                .filter(prm -> prm.getProduct().getId().equals(productId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductRawMaterialDTO convertToDTO(ProductRawMaterial entity) {
        return ProductRawMaterialDTO.builder()
                .id(entity.getId())
                .rawMaterialId(entity.getRawMaterial().getId())
                .rawMaterialName(entity.getRawMaterial().getName())
                .quantityNeeded(entity.getQuantityNeeded())
                .build();
    }
}
