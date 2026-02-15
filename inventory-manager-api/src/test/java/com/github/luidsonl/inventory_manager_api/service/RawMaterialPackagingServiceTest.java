package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingDTO;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import com.github.luidsonl.inventory_manager_api.repository.RawMaterialPackagingRepository;
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
class RawMaterialPackagingServiceTest {

    @Mock
    private RawMaterialPackagingRepository packagingRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialPackagingService packagingService;

    @Test
    @DisplayName("Should return all packagings as DTOs")
    void testFindAll() {
        RawMaterialPackaging packaging = new RawMaterialPackaging();
        packaging.setName("Box 5kg");
        when(packagingRepository.findAll()).thenReturn(Collections.singletonList(packaging));

        List<RawMaterialPackagingDTO> result = packagingService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Box 5kg");
    }

    @Test
    @DisplayName("Should save a packaging using DTO")
    void testSave() {
        RawMaterial material = new RawMaterial();
        material.setId(1L);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        RawMaterialPackaging savedEntity = new RawMaterialPackaging();
        savedEntity.setName("Bag 10kg");
        when(packagingRepository.save(any(RawMaterialPackaging.class))).thenReturn(savedEntity);

        RawMaterialPackagingDTO dto = RawMaterialPackagingDTO.builder()
                .name("Bag 10kg")
                .rawMaterialId(1L)
                .build();

        RawMaterialPackagingDTO result = packagingService.save(dto);

        assertThat(result.getName()).isEqualTo("Bag 10kg");
        verify(packagingRepository, times(1)).save(any(RawMaterialPackaging.class));
    }

    @Test
    @DisplayName("Should update a packaging using DTO")
    void testUpdate() {
        RawMaterialPackaging existing = new RawMaterialPackaging();
        existing.setId(1L);
        existing.setName("Old Box");

        RawMaterialPackagingDTO updatedDTO = RawMaterialPackagingDTO.builder().name("New Box").build();

        when(packagingRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(packagingRepository.save(any(RawMaterialPackaging.class))).thenReturn(existing);

        RawMaterialPackagingDTO result = packagingService.update(1L, updatedDTO);

        assertThat(result.getName()).isEqualTo("New Box");
        verify(packagingRepository, times(1)).save(existing);
    }
}
