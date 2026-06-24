package com.vault.controller;

import com.vault.entity.PasswordCategory;
import com.vault.entity.PasswordEntry;
import com.vault.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    // 获取密码条目列表
    @GetMapping("/list")
    public Map<String, Object> list(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String searchKey,
            @RequestParam(required = false) Integer categoryId) {
        Map<String, Object> result = new HashMap<>();
        List<PasswordEntry> list = passwordService.getEntryList(userId, searchKey, categoryId);
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 新增密码条目
    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody PasswordEntry entry) {
        Map<String, Object> result = new HashMap<>();
        passwordService.addEntry(entry);
        result.put("code", 200);
        result.put("msg", "添加成功");
        result.put("data", entry);
        return result;
    }

    // 编辑密码条目
    @PutMapping("/update")
    public Map<String, Object> update(@RequestBody PasswordEntry entry) {
        Map<String, Object> result = new HashMap<>();
        passwordService.updateEntry(entry);
        result.put("code", 200);
        result.put("msg", "更新成功");
        return result;
    }

    // 删除密码条目
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> delete(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        passwordService.deleteEntry(id);
        result.put("code", 200);
        result.put("msg", "删除成功");
        return result;
    }

    // 获取密码总数
    @GetMapping("/count")
    public Map<String, Object> count(@RequestParam Integer userId) {
        Map<String, Object> result = new HashMap<>();
        int count = passwordService.countByUserId(userId);
        result.put("code", 200);
        result.put("data", count);
        return result;
    }

    // 获取所有密码分类
    @GetMapping("/categories")
    public Map<String, Object> getCategories() {
        Map<String, Object> result = new HashMap<>();
        List<PasswordCategory> list = passwordService.getAllCategories();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    // 新增分类
    @PostMapping("/category/add")
    public Map<String, Object> addCategory(@RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        PasswordCategory category = passwordService.addCategory(body.get("name"));
        result.put("code", 200);
        result.put("msg", "分类添加成功");
        result.put("data", category);
        return result;
    }

    // 删除分类
    @DeleteMapping("/category/delete/{id}")
    public Map<String, Object> deleteCategory(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();
        passwordService.deleteCategory(id);
        result.put("code", 200);
        result.put("msg", "分类删除成功");
        return result;
    }
}
