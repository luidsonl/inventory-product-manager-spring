package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackagingTransaction;
import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialPackagingTransactionRepository
        extends JpaRepository<RawMaterialPackagingTransaction, Long> {
    List<RawMaterialPackagingTransaction> findByPackaging(RawMaterialPackaging packaging);
}
