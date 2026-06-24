package com.vault.mapper;

import com.vault.entity.PasswordCategory;
import com.vault.entity.PasswordEntry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PasswordMapper {

    // 密码条目
    List<PasswordEntry> findAll(@Param("userId") Integer userId, @Param("searchKey") String searchKey, @Param("categoryId") Integer categoryId);
    PasswordEntry findById(@Param("id") Integer id);
    int addEntry(PasswordEntry entry);
    int updateEntry(PasswordEntry entry);
    int deleteEntry(@Param("id") Integer id);
    int countByUserId(@Param("userId") Integer userId);

    // 密码分类
    List<PasswordCategory> findAllCategories();
    int addCategory(PasswordCategory category);
    int deleteCategory(@Param("id") Integer id);
}
