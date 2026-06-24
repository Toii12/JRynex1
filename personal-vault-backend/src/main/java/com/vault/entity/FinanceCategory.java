package com.vault.entity;

import lombok.Data;

@Data
public class FinanceCategory {
    private Integer id;
    private String name;
    private String type; // income / expense
    private Boolean isDefault;
}
