package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductionSuggestionDTO;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductionService productionService;

    private RawMaterial materialA;
    private RawMaterial materialB;

    @BeforeEach
    void setUp() {
        materialA = new RawMaterial();
        materialA.setId(1L);
        materialA.setName("Material A");

        RawMaterialPackaging pkgA1 = new RawMaterialPackaging();
        pkgA1.setCurrentStock(10);
        pkgA1.setQuantityInside(new BigDecimal("5"));

        RawMaterialPackaging pkgA2 = new RawMaterialPackaging();
        pkgA2.setCurrentStock(5);
        pkgA2.setQuantityInside(new BigDecimal("10"));

        materialA.setPackagings(Arrays.asList(pkgA1, pkgA2));

        materialB = new RawMaterial();
        materialB.setId(2L);
        materialB.setName("Material B");

        RawMaterialPackaging pkgB1 = new RawMaterialPackaging();
        pkgB1.setCurrentStock(5);
        pkgB1.setQuantityInside(new BigDecimal("10"));

        materialB.setPackagings(Arrays.asList(pkgB1));
    }

    @Test
    @DisplayName("Should suggest production prioritizing higher price products using packaging stock")
    void testSuggestProductionPriorityWithPackaging() {
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("Expensive Product");
        p1.setPrice(new BigDecimal("500"));
        p1.setFractionable(false);

        ProductRawMaterial prm1 = new ProductRawMaterial();
        prm1.setRawMaterial(materialA);
        prm1.setQuantityNeeded(new BigDecimal("10"));
        p1.setRawMaterials(Arrays.asList(prm1));

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("Cheap Product");
        p2.setPrice(new BigDecimal("100"));
        p2.setFractionable(false);

        ProductRawMaterial prm2 = new ProductRawMaterial();
        prm2.setRawMaterial(materialA);
        prm2.setQuantityNeeded(new BigDecimal("5"));
        p2.setRawMaterials(Arrays.asList(prm2));

        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        ProductionSuggestionDTO suggestion = productionService.suggestProduction();

        assertThat(suggestion.getProducts()).hasSize(1);
        assertThat(suggestion.getProducts().get(0).getProductName()).isEqualTo("Expensive Product");
        assertThat(suggestion.getProducts().get(0).getQuantityToProduce()).isEqualByComparingTo("10");
        assertThat(suggestion.getGrandTotalValue()).isEqualByComparingTo("5000");
    }
}
