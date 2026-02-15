package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackagingTransaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RawMaterialPackagingTransactionRepositoryTest {

    @Autowired
    private RawMaterialPackagingTransactionRepository transactionRepository;

    @Autowired
    private RawMaterialPackagingRepository packagingRepository;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @Test
    @DisplayName("Should find transactions by Packaging")
    void testFindByPackaging() {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setName("Grain");
        rawMaterial.setUnit(MeasureUnitsType.KILOGRAM);
        rawMaterialRepository.save(rawMaterial);

        RawMaterialPackaging packaging = new RawMaterialPackaging();
        packaging.setName("Big Bag");
        packaging.setRawMaterial(rawMaterial);
        packaging.setQuantityInside(new BigDecimal("1000.0"));
        packagingRepository.save(packaging);

        RawMaterialPackagingTransaction transaction = new RawMaterialPackagingTransaction();
        transaction.setPackaging(packaging);
        transaction.setType(TransactionType.INVENTORY_IN);
        transaction.setQuantity(5); // 5 bags
        transaction.setNote("Restock");
        transactionRepository.save(transaction);

        List<RawMaterialPackagingTransaction> transactions = transactionRepository.findByPackaging(packaging);

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getQuantity()).isEqualTo(5);
        assertThat(transactions.get(0).getType()).isEqualTo(TransactionType.INVENTORY_IN);
    }
}
