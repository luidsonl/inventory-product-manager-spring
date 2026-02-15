package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.RawMaterialPackaging;
import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RawMaterialPackagingRepository extends JpaRepository<RawMaterialPackaging, Long> {
    List<RawMaterialPackaging> findByRawMaterial(RawMaterial rawMaterial);

    List<RawMaterialPackaging> findByRawMaterialId(Long rawMaterialId);
}
