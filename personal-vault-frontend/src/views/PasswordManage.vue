<template>
  <div class="password-manage">
    <h2 class="page-title">密码管理</h2>

    <!-- 搜索栏 -->
    <el-card shadow="never" class="filter-card">
      <el-row :gutter="16" align="middle">
        <el-col :span="6">
          <el-input v-model="searchKey" placeholder="搜索名称/网址/用户名" clearable @clear="fetchList" @keyup.enter="fetchList">
            <template #prefix><Search /></template>
          </el-input>
        </el-col>
        <el-col :span="5">
          <el-select v-model="filterCategoryId" placeholder="全部分类" clearable @change="fetchList">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-button type="primary" @click="fetchList">搜索</el-button>
        </el-col>
        <el-col :span="10" style="text-align: right;">
          <el-button type="primary" @click="openDialog()">新增密码</el-button>
          <el-button @click="showCategoryManage = true">管理分类</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 密码列表 -->
    <el-card shadow="never" style="margin-top: 16px;">
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%">
        <el-table-column prop="name" label="名称" min-width="140" />
        <el-table-column prop="url" label="网址" min-width="180">
          <template #default="{ row }">
            <a :href="row.url" target="_blank" v-if="row.url" style="color: #409EFF;">{{ row.url }}</a>
            <span v-else style="color: #c0c4cc;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="password" label="密码" min-width="140">
          <template #default="{ row }">
            <span v-if="!row.showPwd">••••••••</span>
            <span v-else>{{ row.password }}</span>
            <el-button link type="primary" size="small" @click="row.showPwd = !row.showPwd" style="margin-left: 6px;">
              {{ row.showPwd ? '隐藏' : '显示' }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.categoryName || '未分类' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="openDialog(row)">编辑</el-button>
            <el-popconfirm title="确定删除该密码条目？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button link type="danger" size="small">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="名称" required>
          <el-input v-model="form.name" placeholder="如：GitHub" />
        </el-form-item>
        <el-form-item label="网址">
          <el-input v-model="form.url" placeholder="https://..." />
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="form.username" placeholder="用户名/邮箱" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" placeholder="密码" type="password" show-password />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="form.categoryId" placeholder="选择分类" clearable style="width:100%">
            <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>

    <!-- 分类管理对话框 -->
    <el-dialog title="管理分类" v-model="showCategoryManage" width="420px">
      <div style="margin-bottom: 12px;">
        <el-input v-model="newCategoryName" placeholder="输入新分类名称" style="width: 240px;" @keyup.enter="addCategory" />
        <el-button type="primary" @click="addCategory" style="margin-left: 8px;">添加</el-button>
      </div>
      <el-tag
        v-for="c in categories"
        :key="c.id"
        :closable="!c.isDefault"
        @close="deleteCategory(c.id)"
        style="margin: 4px 6px;"
        size="large"
      >{{ c.name }}</el-tag>
      <div v-if="categories.length === 0" style="color: #c0c4cc; text-align:center; padding:20px;">暂无分类</div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import request from '../utils/request'

const userId = () => parseInt(localStorage.getItem('userId') || '0')

const tableData = ref([])
const loading = ref(false)
const searchKey = ref('')
const filterCategoryId = ref(null)
const categories = ref([])
const dialogVisible = ref(false)
const showCategoryManage = ref(false)
const newCategoryName = ref('')

const form = ref({
  id: null, name: '', url: '', username: '', password: '', categoryId: null
})

const dialogTitle = computed(() => form.value.id ? '编辑密码' : '新增密码')

const fetchList = async () => {
  loading.value = true
  try {
    const res = await request.get('/password/list', {
      params: {
        userId: userId(),
        searchKey: searchKey.value || undefined,
        categoryId: filterCategoryId.value || undefined
      }
    })
    tableData.value = (res.data || []).map(r => ({ ...r, showPwd: false }))
  } catch (e) { ElMessage.error('加载失败') }
  finally { loading.value = false }
}

const fetchCategories = async () => {
  const res = await request.get('/password/categories')
  categories.value = res.data || []
}

const openDialog = (row) => {
  if (row) {
    form.value = { ...row, categoryId: row.categoryId }
  } else {
    form.value = { id: null, name: '', url: '', username: '', password: '', categoryId: null }
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  if (!form.value.name) { ElMessage.warning('名称不能为空'); return }
  try {
    const payload = {
      userId: userId(),
      name: form.value.name,
      url: form.value.url,
      username: form.value.username,
      password: form.value.password,
      categoryId: form.value.categoryId || null
    }
    if (form.value.id) {
      await request.put('/password/update', { ...payload, id: form.value.id })
      ElMessage.success('更新成功')
    } else {
      await request.post('/password/add', payload)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    fetchList()
  } catch (e) { ElMessage.error('操作失败') }
}

const handleDelete = async (id) => {
  await request.delete(`/password/delete/${id}`)
  ElMessage.success('删除成功')
  fetchList()
}

const addCategory = async () => {
  if (!newCategoryName.value.trim()) return
  await request.post('/password/category/add', { name: newCategoryName.value.trim() })
  newCategoryName.value = ''
  fetchCategories()
}

const deleteCategory = async (id) => {
  await request.delete(`/password/category/delete/${id}`)
  fetchCategories()
}

onMounted(() => {
  fetchList()
  fetchCategories()
})
</script>

<style scoped>
.password-manage { max-width: 1400px; }
.page-title { font-size: 20px; font-weight: 600; margin-bottom: 16px; color: #303133; }
.filter-card :deep(.el-card__body) { padding: 16px 20px; }
</style>
