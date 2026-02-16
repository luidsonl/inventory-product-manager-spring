package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RawMaterialRepositoryTest {

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Test
    @DisplayName("Should save and retrieve a raw material successfully")
    void testSaveAndFindRawMaterial() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("RM_001");
        rawMaterial.setName("Test Material");
        rawMaterial.setUnit(MeasureUnitsType.KILOGRAM);
        rawMaterial.setFractionable(true);
        rawMaterial.setDescription("A raw material for testing");

        RawMaterial savedMaterial = rawMaterialRepository.save(rawMaterial);

        assertThat(savedMaterial).isNotNull();
        assertThat(savedMaterial.getId()).isNotNull();

        Optional<RawMaterial> foundMaterial = rawMaterialRepository.findById(savedMaterial.getId());
        assertThat(foundMaterial).isPresent();
        assertThat(foundMaterial.get().getName()).isEqualTo("Test Material");
        assertThat(foundMaterial.get().getUnit()).isEqualTo(MeasureUnitsType.KILOGRAM);
    }

    @Test
    @DisplayName("Should find raw material by code")
    void testFindByCode() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("RAW_CODE");
        rawMaterial.setName("Raw Name");
        rawMaterial.setUnit(MeasureUnitsType.GRAM);
        rawMaterialRepository.save(rawMaterial);

        java.util.Optional<RawMaterial> found = rawMaterialRepository.findByCode("RAW_CODE");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Raw Name");
    }
}
