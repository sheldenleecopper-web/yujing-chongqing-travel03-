<template>
  <el-container class="admin-layout">
    <el-aside width="220px" class="aside">
      <div class="logo">渝境智旅后台</div>
      <el-menu :default-active="$route.path" router class="menu" background-color="#001529" text-color="#fff" active-text-color="#409EFF">
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon><span>数据概览</span>
        </el-menu-item>
        <el-menu-item index="/admin/spot">
          <el-icon><Location /></el-icon><span>去处管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/heritage">
          <el-icon><Collection /></el-icon><span>非遗管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/hotel">
          <el-icon><OfficeBuilding /></el-icon><span>酒店管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/user">
          <el-icon><User /></el-icon><span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="welcome">欢迎，{{ userStore.username }}（管理员）</div>
        <el-button type="danger" size="small" @click="handleLogout">退出登录</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出')
  router.push('/admin/login')
}
</script>

<style scoped>
.admin-layout { height: 100vh; }
.aside { background: #001529; }
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}
.menu { border-right: none; }
.header {
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
}
.welcome { color: #303133; }
.main { background: #f0f2f5; padding: 20px; }
</style>
