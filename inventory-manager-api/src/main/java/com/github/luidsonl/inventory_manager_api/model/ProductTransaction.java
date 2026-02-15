package com.github.luidsonl.inventory_manager_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.github.luidsonl.inventory_manager_api.enums.TransactionType;

import jakarta.persistence.*;
import lombok.*;

@Entity 
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(length = 500)
    private String note;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}