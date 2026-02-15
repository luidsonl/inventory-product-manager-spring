package com.github.luidsonl.inventory_manager_api.model;

import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = true, length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private MeasureUnitsType unit;

    @OneToMany(mappedBy = "rawMaterial", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<RawMaterialPackaging> packagings;

    @Column(nullable = false)
    private boolean fractionable = true;
}
