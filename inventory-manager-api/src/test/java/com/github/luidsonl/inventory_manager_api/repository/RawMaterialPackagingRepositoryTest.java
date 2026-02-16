package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RawMaterialPackagingRepositoryTest {

    @Autowired
    private RawMaterialPackagingRepository packagingRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Test
    @DisplayName("Should find packaging by RawMaterial entity")
    void testFindByRawMaterial() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("MILK_001");
        rawMaterial.setName("Milk");
        rawMaterial.setUnit(MeasureUnitsType.LITER);
        rawMaterialRepository.save(rawMaterial);

        RawMaterialPackaging packaging = new RawMaterialPackaging();
        packaging.setName("Box of Milk");
        packaging.setRawMaterial(rawMaterial);
        packaging.setQuantityInside(new BigDecimal("12.0"));
        packaging.setCurrentStock(10);
        packagingRepository.save(packaging);

        List<RawMaterialPackaging> foundList = packagingRepository.findByRawMaterial(rawMaterial);

        assertThat(foundList).hasSize(1);
        assertThat(foundList.get(0).getName()).isEqualTo("Box of Milk");
    }

    @Test
    @DisplayName("Should find packaging by RawMaterial ID")
    void testFindByRawMaterialId() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode("SUGAR_001");
        rawMaterial.setName("Sugar");
        rawMaterial.setUnit(MeasureUnitsType.KILOGRAM);
        rawMaterialRepository.save(rawMaterial);

        RawMaterialPackaging packaging = new RawMaterialPackaging();
        packaging.setName("Sack of Sugar");
        packaging.setRawMaterial(rawMaterial);
        packaging.setQuantityInside(new BigDecimal("50.0"));
        packaging.setCurrentStock(5);
        packagingRepository.save(packaging);

        List<RawMaterialPackaging> foundList = packagingRepository.findByRawMaterialId(rawMaterial.getId());

        assertThat(foundList).hasSize(1);
        assertThat(foundList.get(0).getQuantityInside()).isEqualByComparingTo(new BigDecimal("50.0"));
    }
}
