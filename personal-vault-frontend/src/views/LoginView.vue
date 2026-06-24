<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <div class="login-logo">🔐</div>
      <div class="login-title">Personal Vault</div>
      <div class="login-subtitle">个人密码与财务管理</div>
      <el-form :model="loginForm" :rules="loginRules" ref="loginRef" label-width="80px" class="login-form">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" prefix-icon="User"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '../utils/request'

const loginForm = ref({ username: '', password: '' })
const loading = ref(false)
const loginRef = ref(null)
const router = useRouter()

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  const valid = await loginRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await request.post('/login', loginForm.value)
    if (res.code === 200) {
      localStorage.setItem('token', res.token)
      localStorage.setItem('userId', res.userId)
      ElMessage.success(res.msg)
      router.push('/home')
    } else {
      ElMessage.error(res.msg)
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查后端服务')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
}
.login-card {
  width: 420px;
  padding: 40px 30px 30px;
  border-radius: 12px;
}
.login-logo {
  text-align: center;
  font-size: 48px;
  margin-bottom: 8px;
}
.login-title {
  text-align: center;
  font-size: 24px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}
.login-subtitle {
  text-align: center;
  font-size: 13px;
  color: #909399;
  margin-bottom: 28px;
}
.login-form {
  margin-top: 10px;
}
</style>
