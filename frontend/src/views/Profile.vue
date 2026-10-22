<template>
  <div class="page-wrap">
    <NavBar />
    <div class="container">
      <div class="page-head">
        <span class="sec-dot"></span>
        <h2 class="page-title">个人主页</h2>
      </div>

      <!-- 用户信息横幅 -->
      <el-card class="user-info-card" shadow="never">
        <div class="banner-deco deco-1"></div>
        <div class="banner-deco deco-2"></div>
        <div class="user-info">
          <el-avatar :size="64" class="avatar">{{ userInfo.username ? userInfo.username.charAt(0).toUpperCase() : 'U' }}</el-avatar>
          <div class="user-meta">
            <div class="user-name">{{ userInfo.username || '加载中...' }}</div>
            <div class="user-id">用户ID：{{ userInfo.userId || '-' }}</div>
          </div>
        </div>
      </el-card>

      <el-tabs v-model="activeTab" type="card" class="profile-tabs">
        <el-tab-pane label="我的收藏" name="favorite">
          <el-row :gutter="18" class="tab-grid">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="spot in favorites" :key="spot.id">
              <SpotCard :spot="spot" :is-fav="true" @select="addToSelected" @favorite="removeFav" />
            </el-col>
          </el-row>
          <el-empty v-if="!favorites.length" description="暂无收藏" />
        </el-tab-pane>

        <el-tab-pane label="我的行程" name="trip">
          <div class="trip-header">
            <span>行程中共 {{ tripSpots.length }} 个景点</span>
            <el-button type="danger" @click="goGuideForm" :disabled="!selectedStore.spotIds.length">生成攻略</el-button>
          </div>
          <el-row :gutter="18" v-if="tripSpots.length" class="tab-grid">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="(spot, i) in tripSpots" :key="spot.id">
              <el-card class="trip-card" shadow="never" @click="$router.push(`/spot/${spot.id}`)">
                <div class="trip-num">{{ i + 1 }}</div>
                <div class="trip-name">{{ spot.name }}</div>
                <div class="trip-region">{{ spot.region }}</div>
                <el-button size="small" type="danger" plain @click.stop="removeTripItem(spot)">移除</el-button>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-else description="暂无行程，去景点推荐添加心仪的景点吧" />
        </el-tab-pane>

        <el-tab-pane label="攻略历史记录" name="guide">
          <el-row :gutter="18" class="tab-grid">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="g in guideList" :key="g.id">
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
                  <el-button size="small" @click="delGuide(g.id)">删除</el-button>
                </div>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-if="!guideList.length" description="暂无保存的攻略" />
        </el-tab-pane>

        <el-tab-pane label="攻略图生成记录" name="poster">
          <el-row :gutter="18" class="tab-grid">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="g in posters" :key="g.id">
              <el-card class="poster-card" shadow="never" @click="$router.push(`/guide/detail/${g.id}`)">
                <div class="poster-thumb"><img :src="g.posterUrl" /></div>
                <div class="p-title">{{ g.title }}</div>
                <div class="p-time">{{ formatTime(g.createTime) }}</div>
                <el-button size="small" type="danger" @click.stop="downloadPoster(g)">下载</el-button>
              </el-card>
            </el-col>
          </el-row>
          <el-empty v-if="!posters.length" description="暂无生成记录" />
        </el-tab-pane>

        <el-tab-pane label="历史浏览" name="browse">
          <div class="browse-header">
            <el-button type="danger" text @click="clearBrowse" v-if="browseList.length">清空浏览记录</el-button>
          </div>
          <el-row :gutter="18" class="tab-grid">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="spot in browseList" :key="spot.id">
              <SpotCard :spot="spot" @select="addToSelected" @favorite="addFav" />
            </el-col>
          </el-row>
          <el-empty v-if="!browseList.length" description="暂无浏览记录" />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import SpotCard from '../components/SpotCard.vue'
import { getFavoriteList, removeFavorite, getBrowseHistory, clearBrowseHistory, addFavorite, getPosterRecordList, getUserInfo, getGuideList, deleteGuide, getTrip, removeTripSpot } from '../api'
import { useSelectedStore } from '../store/selected'
import { useRouter } from 'vue-router'

const router = useRouter()
const selectedStore = useSelectedStore()
const activeTab = ref('favorite')
const favorites = ref([])
const posters = ref([])
const browseList = ref([])
const guideList = ref([])
const userInfo = ref({})
const tripSpots = ref([])

onMounted(loadAll)

async function loadAll() {
  try { userInfo.value = await getUserInfo() } catch (e) {}
  try { favorites.value = await getFavoriteList() } catch (e) {}
  try { posters.value = await getPosterRecordList() } catch (e) {}
  try { browseList.value = await getBrowseHistory() } catch (e) {}
  try { guideList.value = await getGuideList() } catch (e) {}
  try {
    // 行程以数据库为准，并同步到本地选择列表以便生成攻略
    const trip = await getTrip()
    tripSpots.value = trip?.spots || []
    tripSpots.value.forEach(s => selectedStore.addSpot(s.id, s.name))
  } catch (e) {}
}

const addToSelected = (spot) => {
  selectedStore.addSpot(spot.id, spot.name)
  ElMessage.success(`已加入行程：${spot.name}`)
}

const removeFav = async (spot) => {
  try {
    await removeFavorite(spot.id)
    favorites.value = favorites.value.filter(s => s.id !== spot.id)
    ElMessage.success('已取消收藏')
  } catch (e) {}
}

const addFav = async (spot) => {
  try { await addFavorite(spot.id); ElMessage.success('已收藏') } catch (e) {}
}

const clearBrowse = async () => {
  try {
    await ElMessageBox.confirm('确定清空所有浏览记录吗？', '提示', { type: 'warning' })
    await clearBrowseHistory()
    browseList.value = []
    ElMessage.success('已清空')
  } catch (e) {}
}

const removeTripItem = async (spot) => {
  try {
    await removeTripSpot(spot.id)
    tripSpots.value = tripSpots.value.filter(s => s.id !== spot.id)
    selectedStore.removeSpot(spot.id)
    ElMessage.success('已从行程中移除')
  } catch (e) {}
}

const goGuideForm = () => {
  router.push('/guide/form')
}

const delGuide = async (id) => {
  try {
    await ElMessageBox.confirm('确定删除该攻略吗？', '提示', { type: 'warning' })
    await deleteGuide(id)
    guideList.value = guideList.value.filter(g => g.id !== id)
    ElMessage.success('已删除')
  } catch (e) {}
}

const formatTime = (t) => t ? new Date(t).toLocaleString('zh-CN') : ''

const downloadPoster = (g) => {
  const a = document.createElement('a')
  a.href = g.posterUrl
  a.download = `${g.title}.png`
  a.click()
}
</script>

<style scoped>
.page-wrap { background: var(--yj-bg); min-height: 100vh; padding-bottom: 56px; }
.container { max-width: 1200px; margin: 0 auto; padding: 28px 20px; }
.page-head { display: flex; align-items: center; gap: 10px; margin-bottom: 20px; }
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

/* ---- 用户信息横幅 ---- */
.user-info-card {
  margin-bottom: 22px;
  border-radius: var(--yj-radius);
  border: none;
  background: linear-gradient(120deg, #2a2320 0%, #4a2e1e 55%, #7a3b1e 100%);
  position: relative;
  overflow: hidden;
  box-shadow: var(--yj-shadow-lg);
}
.banner-deco {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}
.deco-1 {
  right: -50px; top: -70px;
  width: 220px; height: 220px;
  background: radial-gradient(circle, rgba(217, 118, 74, 0.35) 0%, transparent 70%);
}
.deco-2 {
  right: 160px; bottom: -90px;
  width: 180px; height: 180px;
  background: radial-gradient(circle, rgba(193, 154, 91, 0.28) 0%, transparent 70%);
}
.user-info {
  display: flex; align-items: center; gap: 18px;
  padding: 10px 6px;
  position: relative;
  z-index: 1;
}
.avatar {
  background: linear-gradient(135deg, var(--yj-primary), var(--yj-primary-soft));
  color: #fff; font-size: 26px; font-weight: 700;
  border: 3px solid rgba(255, 255, 255, 0.28);
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.25);
  font-family: var(--yj-font-serif);
}
.user-name {
  font-size: 21px; font-weight: 700;
  color: #fff;
  font-family: var(--yj-font-serif);
  letter-spacing: 1px;
}
.user-id { font-size: 13px; color: rgba(255, 255, 255, 0.6); margin-top: 5px; }

/* ---- 标签页 ---- */
.profile-tabs :deep(.el-tabs__header) { margin-bottom: 20px; }
.profile-tabs :deep(.el-tabs__item) {
  border-radius: 10px 10px 0 0 !important;
  transition: all 0.25s;
}
.profile-tabs :deep(.el-tabs__item.is-active) {
  color: var(--yj-primary);
  font-weight: 600;
}
.tab-grid { row-gap: 18px; }

.browse-header { margin-bottom: 16px; }
.trip-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 18px;
  color: var(--yj-ink-2);
  background: var(--yj-card);
  border: 1px solid var(--yj-line);
  padding: 12px 18px;
  border-radius: var(--yj-radius-sm);
  box-shadow: var(--yj-shadow-sm);
}

/* ---- 行程卡 ---- */
.trip-card {
  text-align: center;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.trip-card:hover { transform: translateY(-4px); box-shadow: var(--yj-shadow); }
.trip-num {
  width: 34px; height: 34px;
  margin: 2px auto 10px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--yj-primary), var(--yj-primary-soft));
  color: #fff;
  font-weight: 700;
  display: flex; align-items: center; justify-content: center;
  font-family: var(--yj-font-serif);
}
.trip-name { font-size: 16px; font-weight: 600; margin-bottom: 6px; color: var(--yj-ink); }
.trip-region { font-size: 12px; color: var(--yj-muted); margin-bottom: 14px; }

/* ---- 攻略 / 海报卡 ---- */
.poster-card, .guide-card {
  cursor: pointer;
  text-align: center;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.poster-card:hover, .guide-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--yj-shadow);
}
.poster-thumb {
  height: 200px;
  border-radius: var(--yj-radius-sm);
  overflow: hidden;
  margin-bottom: 12px;
  background: var(--yj-bg);
  border: 1px solid var(--yj-line);
}
.poster-thumb img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.5s ease;
}
.poster-card:hover .poster-thumb img,
.guide-card:hover .poster-thumb img { transform: scale(1.05); }
.poster-thumb.placeholder {
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  color: var(--yj-muted); font-size: 13px; gap: 8px;
}
.p-title { font-size: 15px; font-weight: 600; margin-bottom: 4px; color: var(--yj-ink); }
.p-time { color: var(--yj-muted); font-size: 12px; margin-bottom: 12px; }
.g-title { font-size: 16px; font-weight: 600; margin-bottom: 6px; color: var(--yj-ink); }
.g-meta { color: var(--yj-muted); font-size: 13px; margin-bottom: 4px; }
.g-time { color: var(--yj-muted); font-size: 12px; margin-bottom: 12px; }
.g-actions { display: flex; gap: 6px; justify-content: center; flex-wrap: wrap; }
</style>
