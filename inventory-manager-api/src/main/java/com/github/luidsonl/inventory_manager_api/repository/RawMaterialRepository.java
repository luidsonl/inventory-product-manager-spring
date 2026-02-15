package com.github.luidsonl.inventory_manager_api.repository;

import com.github.luidsonl.inventory_manager_api.model.RawMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
}
