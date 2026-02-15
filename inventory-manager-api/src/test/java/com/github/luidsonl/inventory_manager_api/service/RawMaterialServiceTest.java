package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    @Test
    @DisplayName("Should return all raw materials as DTOs")
    void testFindAll() {
        RawMaterial material = new RawMaterial();
        material.setName("Water");
        when(rawMaterialRepository.findAll()).thenReturn(Collections.singletonList(material));

        List<RawMaterialDTO> result = rawMaterialService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Water");
    }

    @Test
    @DisplayName("Should find raw material by id and return DTO")
    void testFindById() {
        RawMaterial material = new RawMaterial();
        material.setId(1L);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        RawMaterialDTO result = rawMaterialService.findById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should save a raw material from DTO")
    void testSave() {
        RawMaterial material = new RawMaterial();
        material.setName("Sugar");
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(material);

        RawMaterialDTO dto = RawMaterialDTO.builder().name("Sugar").build();
        RawMaterialDTO result = rawMaterialService.save(dto);

        assertThat(result.getName()).isEqualTo("Sugar");
        verify(rawMaterialRepository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    @DisplayName("Should update a raw material using DTO")
    void testUpdate() {
        RawMaterial existing = new RawMaterial();
        existing.setId(1L);
        existing.setName("Old Material");

        RawMaterialDTO updatedDTO = RawMaterialDTO.builder().name("New Material").build();

        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(existing);

        RawMaterialDTO result = rawMaterialService.update(1L, updatedDTO);

        assertThat(result.getName()).isEqualTo("New Material");
        verify(rawMaterialRepository, times(1)).save(existing);
    }

    @Test
    @DisplayName("Should delete a raw material by id")
    void testDeleteById() {
        rawMaterialService.deleteById(1L);
        verify(rawMaterialRepository, times(1)).deleteById(1L);
    }
}
