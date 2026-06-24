<template>
  <div class="dashboard">
    <h2 class="page-title">系统首页</h2>

    <!-- 概览卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #e6f7ff;">
            <Lock style="color: #1890ff; font-size: 28px;" />
          </div>
          <div class="stat-info">
            <div class="stat-label">密码条目</div>
            <div class="stat-value">{{ stats.passwordCount }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #f6ffed;">
            <TrendCharts style="color: #52c41a; font-size: 28px;" />
          </div>
          <div class="stat-info">
            <div class="stat-label">本月收入</div>
            <div class="stat-value income">¥{{ stats.monthIncome }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon" style="background: #fff7e6;">
            <Money style="color: #fa8c16; font-size: 28px;" />
          </div>
          <div class="stat-info">
            <div class="stat-label">本月支出</div>
            <div class="stat-value expense">¥{{ stats.monthExpense }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 本月汇总 -->
    <el-card shadow="hover" class="summary-card">
      <template #header>
        <div class="card-header">
          <span>本月财务概览</span>
          <el-tag type="info">{{ stats.yearMonth }}</el-tag>
        </div>
      </template>
      <div class="summary-body">
        <div class="summary-item">
          <span class="summary-label">总结余</span>
          <span class="summary-value" :class="summarySign >= 0 ? 'income' : 'expense'">
            ¥{{ Math.abs(stats.balance) }}
            <span v-if="summarySign < 0" class="sign">超支</span>
          </span>
        </div>
        <el-divider />
        <el-row :gutter="40">
          <el-col :span="12">
            <div class="detail-item">
              <span>收入</span>
              <span class="income">+¥{{ stats.monthIncome }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <span>支出</span>
              <span class="expense">-¥{{ stats.monthExpense }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Lock, TrendCharts, Money } from '@element-plus/icons-vue'
import request from '../utils/request'

const stats = ref({
  passwordCount: 0,
  monthIncome: 0,
  monthExpense: 0,
  balance: 0,
  yearMonth: ''
})

const summarySign = computed(() => parseFloat(stats.value.balance) || 0)

const userId = () => localStorage.getItem('userId')

onMounted(async () => {
  try {
    const [pwRes, finRes] = await Promise.all([
      request.get('/password/count', { params: { userId: userId() } }),
      request.get('/finance/stats', { params: { userId: userId() } })
    ])
    stats.value.passwordCount = pwRes.data || 0
    if (finRes.data) {
      stats.value.monthIncome = finRes.data.income || 0
      stats.value.monthExpense = finRes.data.expense || 0
      stats.value.balance = finRes.data.balance || 0
      stats.value.yearMonth = finRes.data.yearMonth || ''
    }
  } catch (e) {
    console.error('加载仪表盘数据失败', e)
  }
})
</script>

<style scoped>
.dashboard { max-width: 1200px; }
.page-title { font-size: 20px; font-weight: 600; color: #303133; margin-bottom: 20px; }
.stats-row { margin-bottom: 20px; }
.stat-card { display: flex; align-items: center; }
.stat-card :deep(.el-card__body) { display: flex; align-items: center; gap: 16px; padding: 20px; width: 100%; }
.stat-icon { width: 56px; height: 56px; border-radius: 12px; display: flex; align-items: center; justify-content: center; }
.stat-label { font-size: 13px; color: #909399; margin-bottom: 4px; }
.stat-value { font-size: 28px; font-weight: 700; color: #303133; }
.income { color: #f5222d !important; }
.expense { color: #52c41a !important; }
.summary-card { max-width: 800px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.summary-body { padding: 8px 0; }
.summary-item { display: flex; justify-content: space-between; align-items: center; font-size: 16px; margin-bottom: 12px; }
.summary-value { font-size: 32px; font-weight: 700; }
.sign { font-size: 13px; margin-left: 6px; color: #52c41a; }
.detail-item { display: flex; justify-content: space-between; font-size: 15px; padding: 4px 0; }
</style>
