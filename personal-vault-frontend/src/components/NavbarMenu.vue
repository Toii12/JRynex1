<template>
  <div class="navbar">
    <el-button icon="Menu" type="text" class="collapse-btn" @click="handleCollapse" />
    <div class="navbar-right">
      <span class="brand-title">Personal Vault</span>
      <el-dropdown trigger="click">
        <span class="dropdown-text">
          admin <ArrowDown style="font-size:12px" />
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleLogout" class="logout-item">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowDown } from '@element-plus/icons-vue'

const router = useRouter()
const emit = defineEmits(['collapse'])

const handleCollapse = () => emit('collapse')

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userId')
  router.push('/login')
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.navbar {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
}
.navbar-right {
  display: flex;
  align-items: center;
  gap: 24px;
}
.brand-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a1a2e;
}
.collapse-btn {
  font-size: 18px;
}
.dropdown-text {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
  color: #606266;
  cursor: pointer;
}
.logout-item {
  color: #f56c6c !important;
}
</style>
