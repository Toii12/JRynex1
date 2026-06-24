package com.vault.service;

import com.vault.entity.PasswordCategory;
import com.vault.entity.PasswordEntry;
import com.vault.mapper.PasswordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private PasswordMapper passwordMapper;

    public List<PasswordEntry> getEntryList(Integer userId, String searchKey, Integer categoryId) {
        return passwordMapper.findAll(userId, searchKey, categoryId);
    }

    public PasswordEntry addEntry(PasswordEntry entry) {
        passwordMapper.addEntry(entry);
        return entry;
    }

    public PasswordEntry updateEntry(PasswordEntry entry) {
        passwordMapper.updateEntry(entry);
        return passwordMapper.findById(entry.getId());
    }

    public void deleteEntry(Integer id) {
        passwordMapper.deleteEntry(id);
    }

    public int countByUserId(Integer userId) {
        return passwordMapper.countByUserId(userId);
    }

    // 分类
    public List<PasswordCategory> getAllCategories() {
        return passwordMapper.findAllCategories();
    }

    public PasswordCategory addCategory(String name) {
        PasswordCategory category = new PasswordCategory();
        category.setName(name);
        passwordMapper.addCategory(category);
        return category;
    }

    public void deleteCategory(Integer id) {
        passwordMapper.deleteCategory(id);
    }
}
