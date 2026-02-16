package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingDTO;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialPackagingRepository;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialPackagingService {

    private final RawMaterialPackagingRepository packagingRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public List<RawMaterialPackagingDTO> findAll() {
        return packagingRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RawMaterialPackagingDTO findById(Long id) {
        RawMaterialPackaging packaging = packagingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packaging not found with id: " + id));
        return convertToDTO(packaging);
    }

    @Transactional
    public RawMaterialPackagingDTO save(RawMaterialPackagingDTO dto) {
        RawMaterialPackaging packaging = convertToEntity(dto);
        RawMaterialPackaging saved = packagingRepository.save(packaging);
        return convertToDTO(saved);
    }

    @Transactional
    public RawMaterialPackagingDTO update(Long id, RawMaterialPackagingDTO dto) {
        RawMaterialPackaging existing = packagingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Packaging not found with id: " + id));

        existing.setName(dto.getName());
        existing.setQuantityInside(dto.getQuantityInside());
        existing.setCurrentStock(dto.getCurrentStock());

        if (dto.getRawMaterialId() != null) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Raw Material not found with id: " + dto.getRawMaterialId()));
            existing.setRawMaterial(rawMaterial);
        }

        RawMaterialPackaging saved = packagingRepository.save(existing);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        packagingRepository.deleteById(id);
    }

    public List<RawMaterialPackagingDTO> findByRawMaterialId(Long rawMaterialId) {
        return packagingRepository.findByRawMaterialId(rawMaterialId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private RawMaterialPackagingDTO convertToDTO(RawMaterialPackaging entity) {
        return RawMaterialPackagingDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .rawMaterialId(entity.getRawMaterial() != null ? entity.getRawMaterial().getId() : null)
                .quantityInside(entity.getQuantityInside())
                .currentStock(entity.getCurrentStock())
                .build();
    }

    private RawMaterialPackaging convertToEntity(RawMaterialPackagingDTO dto) {
        RawMaterialPackaging entity = new RawMaterialPackaging();
        entity.setName(dto.getName());
        entity.setQuantityInside(dto.getQuantityInside());
        entity.setCurrentStock(dto.getCurrentStock());

        if (dto.getRawMaterialId() != null) {
            RawMaterial rawMaterial = rawMaterialRepository.findById(dto.getRawMaterialId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException(
                                    "Raw Material not found with id: " + dto.getRawMaterialId()));
            entity.setRawMaterial(rawMaterial);
        }

        return entity;
    }
}
