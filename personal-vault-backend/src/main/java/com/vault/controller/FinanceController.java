package com.vault.controller;

import com.vault.entity.FinanceCategory;
import com.vault.entity.FinanceRecord;
import com.vault.service.FinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    private FinanceService financeService;

    // 获取财务记录列表
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String type) {
        Map<String, Object> result = new HashMap<>();
        List<FinanceRecord> list = financeService.getRecordList(userId, null, categoryId, type);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 新增财务记录
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody FinanceRecord record) {
        Map<String, Object> result = new HashMap<>();
        financeService.addRecord(record);
        result.put("code", 200);
        result.put("msg", "添加成功");
        result.put("data", record);
        return result;
    }

    // 编辑财务记录
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody FinanceRecord record) {
        Map<String, Object> result = new HashMap<>();
        financeService.updateRecord(record);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    // 删除财务记录
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        financeService.deleteRecord(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }

    // 月度统计
    @GetMapping("/stats")
    public Map<String, Object> stats(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        BigDecimal income = financeService.getMonthSum(userId, "income", yearMonth);
        BigDecimal expense = financeService.getMonthSum(userId, "expense", yearMonth);
        Map<String, Object> data = new HashMap<>();
        data.put("income", income);
        data.put("expense", expense);
        data.put("balance", income.subtract(expense));
        data.put("yearMonth", yearMonth);
        result.put("code", 200);
        result.put("data", data);
        return result;
    }

    // 获取财务分类
    @GetMapping("/categories")
    public Map<String, Object> getCategories(@RequestParam String type) {
        Map<String, Object> result = new HashMap<>();
        List<FinanceCategory> list = financeService.getCategoriesByType(type);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 新增分类
    @PostMapping("/category/add")
    public Map<String, Object> addCategory(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        FinanceCategory category = financeService.addCategory(body.get("name"), body.get("type"));
        result.put("code", 200);
        result.put("msg", "分类添加成功");
        result.put("data", category);
        return result;
    }

    // 删除分类
    @DeleteMapping("/category/delete/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        financeService.deleteCategory(id);
        result.put("code", 200);
        result.put("msg", "分类删除成功");
        return result;
    }
}
