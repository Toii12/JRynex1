package com.vault.entity;

import lombok.Data;

@Data
public class PasswordCategory {
    private Integer id;
    private String name;
    private Boolean isDefault;
}
