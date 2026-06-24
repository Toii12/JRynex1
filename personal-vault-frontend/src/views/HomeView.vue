<template>
  <el-container style="height: 100vh;">
    <el-header>
      <NavbarMenu @collapse="handleCollapse" />
    </el-header>
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'" style="background-color: #1a1a2e;">
        <SidebarMenu :collapse="isCollapse" />
      </el-aside>
      <el-main style="padding: 20px; background-color: #f0f2f5;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import NavbarMenu from '../components/NavbarMenu.vue'
import SidebarMenu from '../components/SidebarMenu.vue'

const isCollapse = ref(false)
const handleCollapse = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.el-header {
  padding: 0;
  height: 60px;
  line-height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
}
.el-aside {
  transition: width 0.3s ease;
}
</style>
