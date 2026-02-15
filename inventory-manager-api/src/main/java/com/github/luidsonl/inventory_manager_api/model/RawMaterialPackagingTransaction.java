package com.github.luidsonl.inventory_manager_api.model;

import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity 
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialPackagingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_id", nullable = false)
    private RawMaterialPackaging packaging;

    @Column(nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private LocalDateTime transactionDate;

    @Column(length = 500)
    private String note;

    @PrePersist
    protected void onCreate() {
        if (this.transactionDate == null) {
            this.transactionDate = LocalDateTime.now();
        }
    }
}