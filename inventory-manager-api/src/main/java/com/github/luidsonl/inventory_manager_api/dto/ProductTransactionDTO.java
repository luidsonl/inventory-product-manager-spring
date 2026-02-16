package com.github.luidsonl.inventory_manager_api.dto;

import com.github.luidsonl.inventory_manager_api.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTransactionDTO {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal quantity;
    private TransactionType type;
    private LocalDateTime transactionDate;
    private String note;
}
