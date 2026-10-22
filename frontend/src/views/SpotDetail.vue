<template>
  <div class="page-wrap">
    <NavBar />

    <div class="container" v-if="spot">
      <!-- 返回景点榜 -->
      <div class="crumb">
        <button class="crumb-back" @click="$router.push('/spots')">
          <span class="crumb-arrow">←</span> 返回景点榜
        </button>
        <span class="crumb-rank" v-if="spot.heatIndex">热度 {{ spot.heatIndex }}</span>
      </div>

      <div class="detail-hero">
        <div class="hero-media">
          <img :src="spot.cover" :alt="spot.name" />
          <div class="media-mask"></div>
          <div class="media-title">
            <span class="media-region">
              <el-icon><Location /></el-icon>
              {{ spot.region }}
            </span>
            <h1>{{ spot.name }}</h1>
            <p>{{ spot.intro }}</p>
          </div>
        </div>
      </div>

      <div class="detail-body">
        <div class="main-col">
          <section class="block">
            <h2 class="block-title"><span class="bar"></span>景点介绍</h2>
            <p class="description">{{ spot.description || spot.intro }}</p>
          </section>

          <section class="block" v-if="spot.playTip">
            <h2 class="block-title"><span class="bar"></span>游玩建议</h2>
            <p class="tip-text">{{ spot.playTip }}</p>
          </section>
        </div>

        <aside class="side-col">
          <div class="info-card">
            <h3 class="info-name">{{ spot.name }}</h3>
            <dl class="info-list">
              <div class="info-row">
                <dt>所在区县</dt>
                <dd>{{ spot.region }}</dd>
              </div>
              <div class="info-row">
                <dt>景点分类</dt>
                <dd>{{ sceneText }}</dd>
              </div>
              <div class="info-row">
                <dt>门票</dt>
                <dd :class="{ free: priceText === '免费' }">{{ priceText }}</dd>
              </div>
              <div class="info-row">
                <dt>热度</dt>
                <dd>{{ spot.heatIndex }}</dd>
              </div>
              <div class="info-row" v-if="spot.openTime">
                <dt>开放时间</dt>
                <dd>{{ spot.openTime }}</dd>
              </div>
              <div class="info-row" v-if="spot.address">
                <dt>地址</dt>
                <dd class="address">{{ spot.address }}</dd>
              </div>
            </dl>

            <div class="tag-row" v-if="tags.length">
              <span v-for="tag in tags" :key="tag" class="tag">{{ tag }}</span>
            </div>

            <div class="actions">
              <button class="btn-primary" @click="addToTrip">加入行程</button>
              <button class="btn-ghost" :class="{ active: isFavorited }" @click="toggleFavorite">
                {{ isFavorited ? '已收藏' : '收藏' }}
              </button>
            </div>
          </div>
        </aside>
      </div>
    </div>

    <div class="loading-wrap" v-else>
      <el-skeleton :rows="6" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import { getSpotDetail, addFavorite, removeFavorite, addTripSpot } from '../api'
import { useUserStore } from '../store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const spot = ref(null)
const isFavorited = ref(false)

const tags = computed(() => (spot.value?.tags || '').split(',').filter(Boolean))

const sceneText = computed(() => {
  const map = { nature: '自然风光', history: '人文历史' }
  return map[spot.value?.scene] || '景点'
})

// 门票状态完全依据后端 priceType，未知不展示为免费
const priceText = computed(() => {
  const s = spot.value
  if (!s) return ''
  if (s.priceType === 'unknown' || s.avgCost === null || s.avgCost === undefined) return '价格待确认'
  if (s.priceType === 'free' || Number(s.avgCost) === 0) return '免费'
  return `¥${Number(s.avgCost)}`
})

onMounted(async () => {
  try {
    spot.value = await getSpotDetail(route.params.id)
    isFavorited.value = !!spot.value?.isFavorited
  } catch (e) {
    ElMessage.error('景点信息加载失败')
  }
})

const requireLogin = () => {
  if (userStore.token) return true
  ElMessage.warning('请先登录')
  router.push('/login')
  return false
}

const addToTrip = async () => {
  if (!requireLogin()) return
  try {
    const added = await addTripSpot(spot.value.id)
    if (added) {
      ElMessage.success(`已加入行程：${spot.value.name}`)
    } else {
      ElMessage.info('该景点已在你的行程中')
    }
  } catch (e) {}
}

const toggleFavorite = async () => {
  if (!requireLogin()) return
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
.page-wrap {
  background: #f7f1e7;
  min-height: 100vh;
  padding-bottom: 64px;
}
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 18px 20px 0;
}

/* 面包屑 */
.crumb {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}
.crumb-back {
  border: none;
  background: none;
  padding: 0;
  font-size: 13px;
  color: #80624d;
  cursor: pointer;
  font-family: inherit;
}
.crumb-back:hover { color: var(--yj-primary); }
.crumb-arrow { margin-right: 2px; }
.crumb-rank {
  font-size: 12px;
  color: #9a8c7c;
  letter-spacing: 0.4px;
}

/* 主图 */
.detail-hero {
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid #e9dcc9;
  box-shadow: 0 10px 30px rgba(122, 74, 42, 0.10);
}
.hero-media {
  position: relative;
  height: 380px;
}
.hero-media img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.media-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(32, 22, 14, 0.05) 30%, rgba(32, 22, 14, 0.68) 100%);
}
.media-title {
  position: absolute;
  left: 32px;
  right: 32px;
  bottom: 26px;
  color: #fff;
}
.media-region {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  letter-spacing: 1px;
  color: rgba(255, 248, 238, 0.82);
  margin-bottom: 8px;
}
.media-title h1 {
  margin: 0 0 8px;
  font-family: var(--yj-font-serif);
  font-size: 34px;
  font-weight: 700;
  letter-spacing: 4px;
  text-shadow: 0 2px 14px rgba(0, 0, 0, 0.3);
}
.media-title p {
  margin: 0;
  font-size: 13.5px;
  line-height: 1.7;
  color: rgba(255, 250, 243, 0.88);
}

/* 正文两栏 */
.detail-body {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 340px;
  gap: 26px;
  margin-top: 26px;
  align-items: start;
}
.block {
  background: #fffcf7;
  border: 1px solid #e9dcc9;
  border-radius: 14px;
  padding: 24px 26px;
  margin-bottom: 20px;
  box-shadow: 0 3px 14px rgba(122, 74, 42, 0.05);
}
.block-title {
  display: flex;
  align-items: center;
  gap: 9px;
  margin: 0 0 14px;
  font-family: var(--yj-font-serif);
  font-size: 19px;
  font-weight: 700;
  letter-spacing: 1.5px;
  color: #302920;
}
.block-title .bar {
  width: 3px;
  height: 17px;
  border-radius: 2px;
  background: linear-gradient(180deg, #d95e36, #e8bd94);
}
.description {
  margin: 0;
  font-size: 14.5px;
  line-height: 2;
  color: #57493c;
}
.tip-text {
  margin: 0;
  font-size: 13.5px;
  line-height: 1.9;
  color: #80624d;
  background: rgba(217, 94, 54, 0.05);
  border-left: 2px solid rgba(217, 94, 54, 0.35);
  padding: 12px 14px;
  border-radius: 0 8px 8px 0;
}

/* 信息卡 */
.info-card {
  background: #fffcf7;
  border: 1px solid #e9dcc9;
  border-radius: 14px;
  padding: 24px 22px;
  box-shadow: 0 6px 20px rgba(122, 74, 42, 0.07);
  position: sticky;
  top: 86px;
}
.info-name {
  margin: 0 0 16px;
  font-family: var(--yj-font-serif);
  font-size: 21px;
  letter-spacing: 2px;
  color: #302920;
}
.info-list {
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 11px;
}
.info-row {
  display: flex;
  justify-content: space-between;
  gap: 14px;
  font-size: 13px;
  line-height: 1.6;
}
.info-row dt { color: #9a8c7c; flex-shrink: 0; }
.info-row dd { margin: 0; color: #4a3d31; text-align: right; }
.info-row dd.free { color: #c07a3e; font-weight: 600; }
.info-row dd.address { text-align: right; color: #80624d; }

.tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin: 18px 0 4px;
}
.tag {
  font-size: 11.5px;
  color: #80624d;
  background: rgba(233, 220, 201, 0.5);
  border-radius: 20px;
  padding: 3px 10px;
}

.actions {
  display: flex;
  gap: 10px;
  margin-top: 22px;
}
.btn-primary,
.btn-ghost {
  flex: 1;
  height: 40px;
  border-radius: 9px;
  font-size: 13.5px;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.22s ease;
}
.btn-primary {
  border: none;
  background: #d95e36;
  color: #fff;
  box-shadow: 0 5px 14px rgba(217, 94, 54, 0.24);
}
.btn-primary:hover { background: #c9522d; transform: translateY(-1px); }
.btn-ghost {
  border: 1px solid #e3d3bf;
  background: transparent;
  color: #80624d;
}
.btn-ghost:hover { border-color: rgba(217, 94, 54, 0.5); color: #d95e36; }
.btn-ghost.active {
  border-color: rgba(217, 94, 54, 0.5);
  color: #d95e36;
  background: rgba(217, 94, 54, 0.06);
}

.loading-wrap {
  max-width: 1200px;
  margin: 40px auto;
  padding: 0 20px;
}

@media (max-width: 900px) {
  .detail-body { grid-template-columns: 1fr; }
  .info-card { position: static; }
  .hero-media { height: 280px; }
  .media-title { left: 20px; right: 20px; bottom: 18px; }
  .media-title h1 { font-size: 27px; letter-spacing: 3px; }
}
@media (max-width: 520px) {
  .hero-media { height: 220px; }
  .block { padding: 18px; }
  .actions { flex-direction: column; }
}
@media (prefers-reduced-motion: reduce) {
  .btn-primary, .btn-ghost { transition: none; }
}
</style>
