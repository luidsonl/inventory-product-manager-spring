package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductDTO;
import com.github.luidsonl.inventory_manager_api.dto.ProductRawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRawMaterialRepository;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;
    private final ProductRawMaterialRepository productRawMaterialRepository;

    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO save(ProductDTO dto) {
        Product product = convertToEntity(dto);
        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setFractionable(dto.isFractionable());

        Product saved = productRepository.save(existing);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductRawMaterialDTO addRawMaterial(Long productId, Long rawMaterialId, BigDecimal quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material not found with id: " + rawMaterialId));

        ProductRawMaterial association = new ProductRawMaterial();
        association.setProduct(product);
        association.setRawMaterial(rawMaterial);
        association.setQuantityNeeded(quantity);

        ProductRawMaterial saved = productRawMaterialRepository.save(association);
        return convertToAssociationDTO(saved);
    }

    @Transactional
    public void removeRawMaterial(Long associationId) {
        productRawMaterialRepository.deleteById(associationId);
    }

    private ProductDTO convertToDTO(Product entity) {
        return ProductDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .price(entity.getPrice())
                .fractionable(entity.isFractionable())
                .rawMaterials(entity.getRawMaterials() != null ? entity.getRawMaterials().stream()
                        .map(this::convertToAssociationDTO)
                        .collect(Collectors.toList()) : null)
                .build();
    }

    private ProductRawMaterialDTO convertToAssociationDTO(ProductRawMaterial entity) {
        return ProductRawMaterialDTO.builder()
                .id(entity.getId())
                .rawMaterialId(entity.getRawMaterial().getId())
                .rawMaterialName(entity.getRawMaterial().getName())
                .quantityNeeded(entity.getQuantityNeeded())
                .build();
    }

    private Product convertToEntity(ProductDTO dto) {
        Product entity = new Product();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setFractionable(dto.isFractionable());
        return entity;
    }
}
