<template>
  <div class="finance-manage">
    <h2 class="page-title">财务管理</h2>

    <!-- 月度统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="mini-stat">
          <div class="mini-label">本月收入</div>
          <div class="mini-value income">¥{{ monthStats.income }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="mini-stat">
          <div class="mini-label">本月支出</div>
          <div class="mini-value expense">¥{{ monthStats.expense }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="mini-stat">
          <div class="mini-label">结余</div>
          <div class="mini-value" :class="monthStats.balance >= 0 ? 'income' : 'expense'">¥{{ monthStats.balance }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="mini-stat">
          <div class="mini-label">记录数</div>
          <div class="mini-value" style="color:#303133;">{{ tableData.length }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 筛选栏 -->
    <el-card shadow="never" class="filter-card">
      <el-row :gutter="16" align="middle">
        <el-col :span="4">
          <el-select v-model="filterType" placeholder="全部类型" clearable @change="fetchList">
            <el-option label="支出" value="expense" />
            <el-option label="收入" value="income" />
          </el-select>
        </el-col>
        <el-col :span="5">
          <el-select v-model="filterCategoryId" placeholder="全部分类" clearable @change="fetchList">
            <el-option v-for="c in currentCategories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-col>
        <el-col :span="15" style="text-align: right;">
          <el-button type="success" @click="openDialog('income')">记收入</el-button>
          <el-button type="danger" @click="openDialog('expense')">记支出</el-button>
          <el-button @click="showCategoryManage = true">管理分类</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 记录列表 -->
    <el-card shadow="never" style="margin-top: 16px;">
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="date" label="日期" width="120" sortable />
        <el-table-column label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === 'income' ? 'success' : 'danger'" size="small">
              {{ row.type === 'income' ? '收入' : '支出' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.categoryName || '其他' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="金额" width="140" sortable prop="amount">
          <template #default="{ row }">
            <span :class="row.type === 'income' ? 'income' : 'expense'" style="font-weight:600;">
              {{ row.type === 'income' ? '+' : '-' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDialog(row.type, row)">编辑</el-button>
            <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="440px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="类型">
          <el-radio-group v-model="form.type" :disabled="!!form.id">
            <el-radio value="income">收入</el-radio>
            <el-radio value="expense">支出</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="金额" required>
          <el-input-number v-model="form.amount" :min="0" :precision="2" :step="100" style="width:100%" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable style="width:100%">
            <el-option v-for="c in currentCategories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="日期" required>
          <el-date-picker v-model="form.date" type="date" placeholder="选择日期" style="width:100%" value-format="YYYY-MM-DD" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分类管理 -->
    <el-dialog title="管理分类" v-model="showCategoryManage" width="440px">
      <el-radio-group v-model="categoryType" @change="loadCategories" style="margin-bottom:12px;">
        <el-radio-button value="expense">支出分类</el-radio-button>
        <el-radio-button value="income">收入分类</el-radio-button>
      </el-radio-group>
      <div style="margin-bottom: 12px;">
        <el-input v-model="newCategoryName" placeholder="输入新分类名称" style="width: 240px;" @keyup.enter="addCategory" />
        <el-button type="primary" @click="addCategory" style="margin-left: 8px;">添加</el-button>
      </div>
      <el-tag
        v-for="c in manageCategories"
        :key="c.id"
        :closable="!c.isDefault"
        @close="deleteCategory(c.id)"
        style="margin: 4px 6px;"
        size="large"
      >{{ c.name }}</el-tag>
      <div v-if="manageCategories.length === 0" style="color: #c0c4cc; text-align:center; padding:20px;">暂无分类</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const userId = () => parseInt(localStorage.getItem('userId') || '0')

const tableData = ref([])
const loading = ref(false)
const filterType = ref('')
const filterCategoryId = ref(null)
const dialogVisible = ref(false)
const showCategoryManage = ref(false)
const newCategoryName = ref('')
const categoryType = ref('expense')
// 缓存的分类
const expenseCategories = ref([])
const incomeCategories = ref([])

const currentCategories = computed(() => filterType.value === 'income' ? incomeCategories.value : expenseCategories.value)

const monthStats = ref({ income: 0, expense: 0, balance: 0 })

const formDefault = { id: null, type: 'expense', amount: 0, categoryId: null, date: '' }
const form = ref({ ...formDefault })
const dialogTitle = computed(() => form.value.id ? '编辑记录' : '新增记录')

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/finance/list', {
      params: {
        userId: userId(),
        categoryId: filterCategoryId.value || undefined,
        type: filterType.value || undefined
      }
    })
    tableData.value = res.data || []
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const fetchStats = async () => {
  const res = await request.get('/finance/stats', { params: { userId: userId() } })
  if (res.data) {
    monthStats.value = {
      income: res.data.income || 0,
      expense: res.data.expense || 0,
      balance: res.data.balance || 0
    }
  }
}

const loadCategories = async () => {
  const [expRes, incRes] = await Promise.all([
    request.get('/finance/categories', { params: { type: 'expense' } }),
    request.get('/finance/categories', { params: { type: 'income' } })
  ])
  expenseCategories.value = expRes.data || []
  incomeCategories.value = incRes.data || []
}

const openDialog = (type, row) => {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = { ...formDefault, type, date: new Date().toISOString().slice(0, 10) }
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.value.amount || !form.value.date) { ElMessage.warning('金额和日期不能为空'); return }
  try {
    const payload = {
      userId: userId(),
      type: form.value.type,
      amount: form.value.amount,
      categoryId: form.value.categoryId || null,
      date: form.value.date
    }
    if (form.value.id) {
      await request.put('/finance/update', { ...payload, id: form.value.id })
      ElMessage.success('更新成功')
    } else {
      await request.post('/finance/add', payload)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchList()
    fetchStats()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  await request.delete(`/finance/delete/${id}`)
  ElMessage.success('删除成功')
  fetchList()
  fetchStats()
}

const addCategory = async () => {
  if (!newCategoryName.value.trim()) return
  await request.post('/finance/category/add', { name: newCategoryName.value.trim(), type: categoryType.value })
  newCategoryName.value = ''
  loadCategories()
}

const deleteCategory = async (id) => {
  await request.delete(`/finance/category/delete/${id}`)
  loadCategories()
}

// 分类管理弹窗打开时加载对应分类
const manageCategories = computed(() => categoryType.value === 'income' ? incomeCategories.value : expenseCategories.value)

onMounted(() => {
  fetchList()
  fetchStats()
  loadCategories()
})
</script>

<style scoped>
.finance-manage { max-width: 1400px; }
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; color: #303133; }
.stats-row { margin-bottom: 16px; }
.mini-stat { text-align: center; }
.mini-stat :deep(.el-card__body) { padding: 16px; }
.mini-label { font-size: 13px; color: #909399; margin-bottom: 4px; }
.mini-value { font-size: 24px; font-weight: 700; }
.income { color: #f5222d; }
.expense { color: #52c41a; }
.filter-card :deep(.el-card__body) { padding: 16px 20px; }
</style>
