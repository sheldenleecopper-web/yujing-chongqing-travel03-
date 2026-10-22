<template>
  <div class="page-wrap">
    <NavBar />
    <div class="container">
      <div class="page-head">
        <span class="sec-dot"></span>
        <h2 class="page-title">搜索结果</h2>
      </div>
      <h3 class="kw-title">关键词：<span class="kw">{{ keyword }}</span></h3>

      <!-- 景点/美食/快闪结果 -->
      <section class="result-section">
        <div class="result-title">
          <h3><span class="mini-dot"></span>景点 / 美食 / 活动 <el-tag size="small" type="info">{{ spotResults.length }}</el-tag></h3>
        </div>
        <el-row :gutter="18" v-if="spotResults.length" class="result-grid">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="spot in spotResults" :key="spot.id">
            <SpotCard :spot="spot" @select="addToSelected" @favorite="toggleFavorite" />
          </el-col>
        </el-row>
        <el-empty v-else description="暂无相关去处" :image-size="80" />
      </section>

      <!-- 非遗结果 -->
      <section class="result-section">
        <div class="result-title">
          <h3><span class="mini-dot"></span>非遗文化 <el-tag size="small" type="info">{{ heritageResults.length }}</el-tag></h3>
        </div>
        <el-row :gutter="18" v-if="heritageResults.length" class="result-grid">
          <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="h in heritageResults" :key="h.id">
            <el-card class="heritage-card" shadow="never" @click="goHeritage">
              <div class="h-cover"><img :src="h.cover" :alt="h.name" /></div>
              <div class="h-body">
                <div class="h-name">{{ h.name }}</div>
                <div class="h-tags">
                  <el-tag :type="h.level === '国家级' ? 'danger' : 'warning'" size="small">{{ h.level }}</el-tag>
                  <el-tag size="small">{{ h.category }}</el-tag>
                </div>
                <div class="h-inheritor">传承人：{{ h.inheritor || '暂无' }}</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-empty v-else description="暂无相关非遗" :image-size="80" />
      </section>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import SpotCard from '../components/SpotCard.vue'
import { searchSpots, searchHeritage, addFavorite } from '../api'
import { useSelectedStore } from '../store/selected'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const selectedStore = useSelectedStore()
const userStore = useUserStore()

const keyword = ref('')
const spotResults = ref([])
const heritageResults = ref([])

onMounted(() => {
  keyword.value = route.query.keyword || ''
  doSearch()
})

watch(() => route.query.keyword, (val) => {
  keyword.value = val || ''
  doSearch()
})

async function doSearch() {
  const kw = keyword.value.trim()
  if (!kw) {
    spotResults.value = []
    heritageResults.value = []
    return
  }
  try {
    spotResults.value = await searchSpots(kw)
  } catch (e) { spotResults.value = [] }
  try {
    heritageResults.value = await searchHeritage(kw)
  } catch (e) { heritageResults.value = [] }
}

const addToSelected = (spot) => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  selectedStore.addSpot(spot.id, spot.name)
  ElMessage.success(`已加入行程：${spot.name}`)
}

const toggleFavorite = async (spot) => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  try {
    await addFavorite(spot.id)
    ElMessage.success('已收藏')
  } catch (e) {}
}

const goHeritage = () => {
  router.push('/heritage')
}
</script>

<style scoped>
.page-wrap { background: var(--yj-bg); min-height: 100vh; padding-bottom: 56px; }
.container { max-width: 1200px; margin: 0 auto; padding: 28px 20px; }
.page-head { display: flex; align-items: center; gap: 10px; margin-bottom: 14px; }
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
.kw-title {
  font-size: 15px; color: var(--yj-ink-2);
  margin: 0 0 26px; font-weight: 400;
  background: var(--yj-card);
  border: 1px solid var(--yj-line);
  display: inline-block;
  padding: 8px 16px;
  border-radius: 20px;
  box-shadow: var(--yj-shadow-sm);
}
.kw { color: var(--yj-primary); font-weight: 700; }

.result-section { margin-bottom: 38px; }
.result-title { margin-bottom: 16px; }
.result-title h3 {
  font-size: 17px; color: var(--yj-ink);
  display: flex; align-items: center; gap: 8px;
  font-weight: 700;
  font-family: var(--yj-font-serif);
  letter-spacing: 1px;
  margin: 0;
}
.mini-dot {
  width: 4px; height: 15px;
  background: linear-gradient(180deg, var(--yj-primary), var(--yj-gold));
  border-radius: 2px;
}
.result-grid { row-gap: 18px; }

.heritage-card {
  cursor: pointer;
  border-radius: var(--yj-radius);
  overflow: hidden;
  border: 1px solid var(--yj-line);
  transition: transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1), box-shadow 0.35s ease;
}
.heritage-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--yj-shadow-lg);
}
.heritage-card :deep(.el-card__body) { padding: 0; }
.h-cover { height: 164px; overflow: hidden; }
.h-cover img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.6s ease;
}
.heritage-card:hover .h-cover img { transform: scale(1.07); }
.h-body { padding: 13px 14px 15px; }
.h-name {
  font-size: 16px; font-weight: 700; margin-bottom: 8px;
  color: var(--yj-ink);
  font-family: var(--yj-font-serif);
}
.h-tags { display: flex; gap: 6px; margin-bottom: 8px; }
.h-inheritor { color: var(--yj-muted); font-size: 13px; }
</style>
