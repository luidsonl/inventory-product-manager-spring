package com.github.luidsonl.inventory_manager_api.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "raw_material_id", nullable = false)
    private RawMaterial rawMaterial;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantityNeeded;
}
