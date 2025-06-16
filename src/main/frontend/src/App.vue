<template>
  <div id="app">
    <el-container class="layout-container">
      <!-- Header -->
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <el-icon class="logo-icon"><DataAnalysis /></el-icon>
            <span class="logo-text">CTKB</span>
          </div>
          <div class="header-nav">
            <el-menu
              :default-active="$route.path"
              mode="horizontal"
              router
              class="header-menu"
            >
              <el-menu-item index="/dashboard">
                <el-icon><Odometer /></el-icon>
                Dashboard
              </el-menu-item>
              <el-menu-item index="/conditions">
                <el-icon><Document /></el-icon>
                Conditions
              </el-menu-item>
              <el-menu-item index="/criteria">
                <el-icon><List /></el-icon>
                Criteria
              </el-menu-item>
              <el-menu-item index="/analytics">
                <el-icon><TrendCharts /></el-icon>
                Analytics
              </el-menu-item>
            </el-menu>
          </div>
        </div>
      </el-header>

      <!-- Main Content -->
      <el-main class="main-content">
        <router-view />
      </el-main>

      <!-- Footer -->
      <el-footer class="footer">
        <div class="footer-content">
          <span>&copy; 2024 Clinical Trial Knowledge Base. All rights reserved.</span>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup>
import { DataAnalysis, Odometer, Document, List, TrendCharts } from '@element-plus/icons-vue'
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 0;
  height: 60px !important;
}

.header-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
}

.logo-icon {
  margin-right: 8px;
  font-size: 24px;
}

.header-menu {
  border-bottom: none;
}

.main-content {
  background-color: #f5f7fa;
  padding: 20px;
  min-height: calc(100vh - 120px);
}

.footer {
  background-color: #fff;
  border-top: 1px solid #e4e7ed;
  height: 60px !important;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer-content {
  color: #909399;
  font-size: 14px;
}
</style>

<style>
body {
  margin: 0;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

#app {
  min-height: 100vh;
}
</style>
