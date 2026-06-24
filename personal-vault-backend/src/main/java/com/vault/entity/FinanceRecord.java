package com.vault.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FinanceRecord {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String type; // income / expense
    private BigDecimal amount;
    private LocalDate date;
    private LocalDateTime createdAt;

    // 联表查询字段
    private String categoryName;
}
