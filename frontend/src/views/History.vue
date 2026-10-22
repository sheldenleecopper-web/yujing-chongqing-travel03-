<template>
  <div class="page-wrap">
    <NavBar />
    <div class="container">
      <div class="page-head">
        <span class="sec-dot"></span>
        <h2 class="page-title">我的攻略记录</h2>
      </div>
      <el-row :gutter="18" class="card-grid">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="g in list" :key="g.id">
          <el-card class="guide-card" shadow="never">
            <div class="poster-thumb" v-if="g.posterUrl">
              <img :src="g.posterUrl" />
            </div>
            <div class="poster-thumb placeholder" v-else>
              <el-icon :size="40" color="#ccc"><Document /></el-icon>
              <span>暂无海报</span>
            </div>
            <div class="g-title">{{ g.title }}</div>
            <div class="g-meta">{{ g.paramsSummary }}</div>
            <div class="g-time">{{ formatTime(g.createTime) }}</div>
            <div class="g-actions">
              <el-button size="small" type="primary" @click="$router.push(`/guide/detail/${g.id}`)">查看</el-button>
              <el-button size="small" type="danger" @click="downloadPoster(g)" v-if="g.posterUrl">下载海报</el-button>
              <el-button size="small" @click="del(g.id)">删除</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-if="!list.length" description="暂无保存的攻略" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import { getGuideList, deleteGuide } from '../api'

const list = ref([])

onMounted(async () => {
  try {
    list.value = await getGuideList()
  } catch (e) {}
})

const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

const downloadPoster = (g) => {
  const a = document.createElement('a')
  a.href = g.posterUrl
  a.download = `${g.title}.png`
  a.click()
}

const del = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该攻略吗？', '提示', { type: 'warning' })
    await deleteGuide(id)
    list.value = list.value.filter(g => g.id !== id)
    ElMessage.success('已删除')
  } catch (e) {}
}
</script>

<style scoped>
.page-wrap { background: var(--yj-bg); min-height: 100vh; padding-bottom: 56px; }
.container { max-width: 1200px; margin: 0 auto; padding: 28px 20px; }
.page-head { display: flex; align-items: center; gap: 10px; margin-bottom: 24px; }
.sec-dot {
  width: 4px; height: 22px;
  background: linear-gradient(180deg, var(--yj-primary), var(--yj-gold));
  border-radius: 2px;
}
.page-title {
  font-family: var(--yj-font-serif);
  font-size: 24px; font-weight: 700;
  color: var(--yj-ink); margin: 0;
  letter-spacing: 2px;
}
.card-grid { row-gap: 18px; }
.guide-card {
  text-align: center;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.guide-card:hover { transform: translateY(-4px); box-shadow: var(--yj-shadow); }
.poster-thumb {
  height: 200px; border-radius: var(--yj-radius-sm);
  overflow: hidden; margin-bottom: 12px;
  background: var(--yj-bg);
  border: 1px solid var(--yj-line);
}
.poster-thumb img { width: 100%; height: 100%; object-fit: cover; }
.poster-thumb.placeholder {
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  color: var(--yj-muted); font-size: 13px; gap: 8px;
}
.g-title { font-size: 16px; font-weight: 600; margin-bottom: 6px; color: var(--yj-ink); }
.g-meta { color: var(--yj-muted); font-size: 13px; margin-bottom: 4px; }
.g-time { color: var(--yj-muted); font-size: 12px; margin-bottom: 12px; }
.g-actions { display: flex; gap: 6px; justify-content: center; flex-wrap: wrap; }
</style>
