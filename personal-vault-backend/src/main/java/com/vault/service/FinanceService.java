package com.vault.service;

import com.vault.entity.FinanceCategory;
import com.vault.entity.FinanceRecord;
import com.vault.mapper.FinanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FinanceService {

    @Autowired
    private FinanceMapper financeMapper;

    public List<FinanceRecord> getRecordList(Integer userId, String searchKey, Integer categoryId, String type) {
        return financeMapper.findAll(userId, searchKey, categoryId, type);
    }

    public FinanceRecord addRecord(FinanceRecord record) {
        financeMapper.addRecord(record);
        return record;
    }

    public FinanceRecord updateRecord(FinanceRecord record) {
        financeMapper.updateRecord(record);
        return financeMapper.findById(record.getId());
    }

    public void deleteRecord(Integer id) {
        financeMapper.deleteRecord(id);
    }

    public BigDecimal getMonthSum(Integer userId, String type, String yearMonth) {
        return financeMapper.sumByTypeAndMonth(userId, type, yearMonth);
    }

    // 分类
    public List<FinanceCategory> getCategoriesByType(String type) {
        return financeMapper.findAllCategories(type);
    }

    public FinanceCategory addCategory(String name, String type) {
        FinanceCategory category = new FinanceCategory();
        category.setName(name);
        category.setType(type);
        financeMapper.addCategory(category);
        return category;
    }

    public void deleteCategory(Integer id) {
        financeMapper.deleteCategory(id);
    }
}
