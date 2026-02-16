package com.github.luidsonl.inventory_manager_api.service;

import com.github.luidsonl.inventory_manager_api.dto.ProductionExecutionDTO;
import com.github.luidsonl.inventory_manager_api.model.Product;
import com.github.luidsonl.inventory_manager_api.model.ProductRawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionServiceLogicTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductTransactionService productTransactionService;
    @Mock
    private RawMaterialPackagingTransactionService packagingTransactionService;

    @InjectMocks
    private ProductionService productionService;

    @Test
    @DisplayName("Should calculate requirements for a product")
    void testCalculateRequirements() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Cookie");

        RawMaterial sugar = new RawMaterial();
        sugar.setId(10L);
        sugar.setName("Sugar");
        sugar.setUnit(com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType.KILOGRAM);

        ProductRawMaterial prm = new ProductRawMaterial();
        prm.setProduct(product);
        prm.setRawMaterial(sugar);
        prm.setQuantityNeeded(new BigDecimal("0.5"));

        product.setRawMaterials(List.of(prm));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var result = productionService.calculateRequirements(1L, new BigDecimal("10"));

        assertThat(result.getProductName()).isEqualTo("Cookie");
        assertThat(result.getMaterials()).hasSize(1);
        assertThat(result.getMaterials().get(0).getTotalNeededQuantity()).isEqualByComparingTo(new BigDecimal("5.0"));
    }

    @Test
    @DisplayName("Should call transaction services during execution")
    void testExecuteProduction() {
        ProductionExecutionDTO execution = ProductionExecutionDTO.builder()
                .note("Test")
                .producedProducts(List.of(new ProductionExecutionDTO.ProducedProductInputDTO(1L, BigDecimal.TEN)))
                .consumedMaterials(List.of(new ProductionExecutionDTO.ConsumedMaterialInputDTO(1L, 5)))
                .build();

        productionService.executeProduction(execution);

        verify(productTransactionService, times(1)).save(any());
        verify(packagingTransactionService, times(1)).save(any());
    }
}
