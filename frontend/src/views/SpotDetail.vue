<template>
  <div class="page-wrap">
    <NavBar />
    <div class="container" v-if="spot">
      <div class="page-head">
        <span class="sec-dot"></span>
        <h2 class="page-title">{{ spot.name }}</h2>
      </div>
      <el-row :gutter="24" class="detail">
        <el-col :xs="24" :md="14">
          <div class="main-img-wrap">
            <img :src="spot.cover" class="main-img" />
          </div>
          <div class="intro">
            <h3><span class="intro-dot"></span>简介</h3>
            <p>{{ spot.intro }}</p>
          </div>
        </el-col>
        <el-col :xs="24" :md="10">
          <el-card class="info-card" shadow="never">
            <h2>{{ spot.name }}</h2>
            <div class="meta">
              <p><el-icon><Guide /></el-icon> 类型：{{ typeText }}</p>
              <p><el-icon><Location /></el-icon> {{ spot.region }} · {{ spot.address }}</p>
              <p><el-icon><Clock /></el-icon> {{ spot.openTime || '全天开放' }}</p>
              <p v-if="spot.avgCost > 0"><el-icon><Money /></el-icon> 人均 ¥{{ spot.avgCost }}</p>
              <p><el-icon><Fire /></el-icon> 热度 {{ spot.heatIndex }}</p>
            </div>
            <div class="tags">
              <el-tag v-for="tag in tags" :key="tag" type="danger" effect="plain">{{ tag }}</el-tag>
            </div>
            <div class="actions">
              <el-button type="primary" size="large" @click="addToSelected">加入行程</el-button>
              <el-button type="danger" size="large" @click="toggleFavorite">{{ isFavorited ? '已收藏' : '收藏' }}</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import { getSpotDetail, addFavorite, removeFavorite } from '../api'
import { useSelectedStore } from '../store/selected'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const selectedStore = useSelectedStore()
const userStore = useUserStore()

const spot = ref(null)
const isFavorited = ref(false)
const tags = computed(() => (spot.value?.tags || '').split(',').filter(Boolean))
const typeText = computed(() => {
  const map = { attraction: '景点', food: '美食', activity: '快闪活动' }
  return map[spot.value?.type] || spot.value?.type || ''
})

onMounted(async () => {
  try {
    spot.value = await getSpotDetail(route.params.id)
    isFavorited.value = !!spot.value?.isFavorited
  } catch (e) {}
})

const addToSelected = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  selectedStore.addSpot(spot.value.id, spot.value.name)
  ElMessage.success(`已加入行程：${spot.value.name}`)
}

const toggleFavorite = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    if (isFavorited.value) {
      await removeFavorite(spot.value.id)
      isFavorited.value = false
      ElMessage.success('已取消收藏')
    } else {
      await addFavorite(spot.value.id)
      isFavorited.value = true
      ElMessage.success('已收藏')
    }
  } catch (e) {}
}
</script>

<style scoped>
.page-wrap { background: var(--yj-bg); min-height: 100vh; padding-bottom: 56px; }
.container { max-width: 1200px; margin: 0 auto; padding: 28px 20px; }
.page-head { display: flex; align-items: center; gap: 10px; margin-bottom: 18px; }
.sec-dot {
  width: 4px; height: 22px;
  background: linear-gradient(180deg, var(--yj-primary), var(--yj-gold));
  border-radius: 2px;
}
.page-title {
  font-family: var(--yj-font-serif);
  font-size: 24px; font-weight: 700;
  color: var(--yj-ink); margin: 0;
  letter-spacing: 1px;
}
.detail { margin-top: 20px; }

.main-img-wrap {
  border-radius: var(--yj-radius);
  overflow: hidden;
  box-shadow: var(--yj-shadow);
  margin-bottom: 22px;
  border: 1px solid var(--yj-line);
}
.main-img { width: 100%; display: block; }

.intro {
  background: var(--yj-card);
  border: 1px solid var(--yj-line);
  border-radius: var(--yj-radius);
  padding: 20px 22px;
  box-shadow: var(--yj-shadow-sm);
}
.intro h3 {
  margin: 0 0 12px;
  color: var(--yj-ink);
  font-family: var(--yj-font-serif);
  letter-spacing: 1px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.intro-dot {
  width: 4px; height: 16px;
  background: linear-gradient(180deg, var(--yj-primary), var(--yj-gold));
  border-radius: 2px;
}
.intro p { line-height: 2; color: var(--yj-ink-2); margin: 0; }

.info-card {
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow-sm);
  position: sticky;
  top: 88px;
}
.info-card h2 {
  font-family: var(--yj-font-serif);
  color: var(--yj-ink);
  margin: 0 0 16px;
  letter-spacing: 1px;
}
.meta p {
  display: flex; align-items: center; gap: 8px;
  margin: 0 0 10px;
  color: var(--yj-ink-2);
}
.meta .el-icon { color: var(--yj-primary); }
.tags { margin: 16px 0; display: flex; gap: 6px; flex-wrap: wrap; }
.actions { display: flex; gap: 12px; margin-top: 20px; }
.actions .el-button { flex: 1; }
</style>
