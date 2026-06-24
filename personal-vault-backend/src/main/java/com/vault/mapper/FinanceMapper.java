package com.vault.mapper;

import com.vault.entity.FinanceCategory;
import com.vault.entity.FinanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface FinanceMapper {

    // 财务记录
    List<FinanceRecord> findAll(@Param("userId") Integer userId, @Param("searchKey") String searchKey, @Param("categoryId") Integer categoryId, @Param("type") String type);
    FinanceRecord findById(@Param("id") Integer id);
    int addRecord(FinanceRecord record);
    int updateRecord(FinanceRecord record);
    int deleteRecord(@Param("id") Integer id);

    // 月度统计
    BigDecimal sumByTypeAndMonth(@Param("userId") Integer userId, @Param("type") String type, @Param("yearMonth") String yearMonth);

    // 财务分类
    List<FinanceCategory> findAllCategories(@Param("type") String type);
    int addCategory(FinanceCategory category);
    int deleteCategory(@Param("id") Integer id);
}
