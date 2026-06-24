package com.vault.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PasswordEntry {
    private Integer id;
    private Integer userId;
    private Integer categoryId;
    private String name;
    private String url;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 联表查询字段
    private String categoryName;
}
