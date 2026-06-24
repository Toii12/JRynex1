<template>
  <el-menu
    default-active="/home/dashboard"
    class="sidebar-menu"
    background-color="#1a1a2e"
    text-color="#bfcbd9"
    active-text-color="#409EFF"
    :collapse="isCollapse"
    router
  >
    <el-menu-item index="/home/dashboard">
      <HomeFilled />
      <template #title>系统首页</template>
    </el-menu-item>
    <el-menu-item index="/home/passwords">
      <Lock />
      <template #title>密码管理</template>
    </el-menu-item>
    <el-menu-item index="/home/finance">
      <Wallet />
      <template #title>财务管理</template>
    </el-menu-item>
  </el-menu>
</template>

<script setup>
import { ref, watch } from 'vue'
import { HomeFilled, Lock, Wallet } from '@element-plus/icons-vue'

const isCollapse = ref(false)
const props = defineProps({
  collapse: { type: Boolean, default: false }
})
watch(() => props.collapse, (val) => { isCollapse.value = val })
</script>

<style scoped>
.sidebar-menu {
  height: 100vh;
  border-right: none;
}
</style>
