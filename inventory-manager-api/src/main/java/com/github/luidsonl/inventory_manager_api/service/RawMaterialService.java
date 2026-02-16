package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RawMaterialService {

    private final RawMaterialRepository rawMaterialRepository;

    public List<RawMaterialDTO> findAll() {
        return rawMaterialRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material not found with id: " + id));
        return convertToDTO(rawMaterial);
    }

    @Transactional
    public RawMaterialDTO save(RawMaterialDTO rawMaterialDTO) {
        RawMaterial rawMaterial = convertToEntity(rawMaterialDTO);
        RawMaterial saved = rawMaterialRepository.save(rawMaterial);
        return convertToDTO(saved);
    }

    @Transactional
    public RawMaterialDTO update(Long id, RawMaterialDTO rawMaterialDTO) {
        RawMaterial existing = rawMaterialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Raw Material not found with id: " + id));

        existing.setCode(rawMaterialDTO.getCode());
        existing.setName(rawMaterialDTO.getName());
        existing.setDescription(rawMaterialDTO.getDescription());
        existing.setUnit(rawMaterialDTO.getUnit());
        existing.setFractionable(rawMaterialDTO.isFractionable());

        RawMaterial saved = rawMaterialRepository.save(existing);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteById(Long id) {
        rawMaterialRepository.deleteById(id);
    }

    private RawMaterialDTO convertToDTO(RawMaterial rawMaterial) {
        return RawMaterialDTO.builder()
                .id(rawMaterial.getId())
                .code(rawMaterial.getCode())
                .name(rawMaterial.getName())
                .description(rawMaterial.getDescription())
                .unit(rawMaterial.getUnit())
                .fractionable(rawMaterial.isFractionable())
                .build();
    }

    private RawMaterial convertToEntity(RawMaterialDTO dto) {
        RawMaterial entity = new RawMaterial();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setUnit(dto.getUnit());
        entity.setFractionable(dto.isFractionable());
        return entity;
    }
}
