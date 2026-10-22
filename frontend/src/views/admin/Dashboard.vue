<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="用户总数" :value="stats.userCount" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="去处总数" :value="stats.spotCount" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="非遗总数" :value="stats.heritageCount" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="攻略总数" :value="stats.guideCount" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAdminStats } from '../../api'

const stats = ref({ userCount: 0, spotCount: 0, heritageCount: 0, guideCount: 0 })

onMounted(async () => {
  stats.value = await getAdminStats()
})
</script>

<style scoped>
.dashboard { padding: 0; }
</style>
