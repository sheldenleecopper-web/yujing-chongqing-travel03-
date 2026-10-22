<template>
  <div class="ranking-page">
    <NavBar />

    <!-- ================= 城市横幅 ================= -->
    <section class="hero">
      <img class="hero-img" :src="bannerImg" alt="重庆城市天际线与江景" />
      <div class="hero-veil"></div>
      <div class="hero-inner">
        <div class="hero-copy">
          <span class="hero-eyebrow">SPOT RANKING</span>
          <h1 class="hero-title">景点推荐</h1>
          <p class="hero-sub">山城重庆 · 精选品质景点等你来探索</p>
        </div>
        <p class="hero-poem">一座城<br />待你慢慢看</p>
      </div>
    </section>

    <!-- ================= 主体 ================= -->
    <div class="page-body">
      <!-- 左侧水墨装饰：可整体关闭（v-if="showDecor"） -->
      <aside v-if="showDecor" class="decor decor-left" aria-hidden="true">
        <div class="decor-seal">山城之美</div>
        <p class="decor-verse">一城山水<br />一城烟火<br />一生诗意<br />一生生活</p>
        <svg class="decor-ink decor-ink-left" viewBox="0 0 180 260" fill="none">
          <path d="M4 214c22-6 32-30 48-32 18-2 22 16 38 12 16-4 26-34 44-38 14-4 24 8 42 4" stroke="currentColor" stroke-width="1.1" />
          <path d="M0 232c26-4 34-20 54-18 20 2 26 14 46 10 18-4 28-22 46-24 12-2 22 4 34 2" stroke="currentColor" stroke-width="0.9" />
          <path d="M28 178c10-16 18-40 32-42 16-2 20 22 34 20 10-2 14-18 24-24" stroke="currentColor" stroke-width="0.8" />
          <path d="M44 120c6-8 8-18 14-20 6-2 10 12 16 10 6-2 8-14 14-18" stroke="currentColor" stroke-width="0.7" />
          <circle cx="146" cy="60" r="14" stroke="currentColor" stroke-width="0.8" />
        </svg>
      </aside>

      <!-- 右侧水墨远山 -->
      <aside v-if="showDecor" class="decor decor-right" aria-hidden="true">
        <svg class="decor-ink decor-ink-right" viewBox="0 0 200 240" fill="none">
          <path d="M2 196c26-4 34-38 58-40 22-2 30 26 52 24 24-2 34-44 60-46 12-1 20 6 26 10" stroke="currentColor" stroke-width="1.1" />
          <path d="M0 216c30-2 40-24 64-24 24 0 32 18 56 16 24-2 34-26 58-28 8-1 16 2 22 4" stroke="currentColor" stroke-width="0.9" />
          <path d="M34 156c14-12 22-30 38-30 16 0 22 20 40 18 14-2 22-18 38-20" stroke="currentColor" stroke-width="0.8" />
        </svg>
      </aside>

      <main class="rank-main">
        <!-- ================= 景点榜筛选栏 ================= -->
        <div class="filter-bar">
          <div class="list-entry">
            <span class="list-mark"></span>
            景点榜
          </div>
          <span class="filter-divider"></span>

          <nav class="filter-tabs" aria-label="景点分类">
            <button
              v-for="item in categories"
              :key="item.key"
              class="filter-tab"
              :class="{ active: category === item.key }"
              @click="changeCategory(item.key)"
            >
              {{ item.label }}
            </button>
          </nav>

          <div class="filter-right">
            <el-select
              v-model="sort"
              class="sort-select"
              size="small"
              :teleported="false"
              @change="changeSort"
            >
              <el-option label="热度优先" value="heat" />
              <el-option label="价格从低到高" value="priceAsc" />
              <el-option label="价格从高到低" value="priceDesc" />
            </el-select>
          </div>
        </div>

        <!-- 搜索结果提示（与筛选组合） -->
        <div v-if="keyword" class="search-note">
          搜索「<strong>{{ keyword }}</strong>」，共 {{ total }} 个景点
          <button class="clear-btn" @click="clearKeyword">清除搜索</button>
        </div>

        <!-- ================= 景点卡片 ================= -->
        <div v-loading="loading" class="card-grid">
          <SpotRankCard
            v-for="(spot, i) in spots"
            :key="spot.id"
            :spot="spot"
            :rank="(page - 1) * pageSize + i + 1"
            :index="i"
          />
        </div>

        <div v-if="!loading && !spots.length" class="empty-wrap">
          <p class="empty-title">没有找到符合条件的景点</p>
          <p class="empty-tip">试着更换分类或清除搜索关键词</p>
          <button class="empty-btn" @click="resetAll">查看全部景点</button>
        </div>

        <!-- 分页：保持低调，不打断卡片节奏 -->
        <nav v-if="totalPages > 1" class="pager" aria-label="分页">
          <button :disabled="page <= 1" @click="goPage(page - 1)">上一页</button>
          <button
            v-for="p in pageList"
            :key="p"
            :class="{ active: p === page }"
            @click="goPage(p)"
          >
            {{ p }}
          </button>
          <button :disabled="page >= totalPages" @click="goPage(page + 1)">下一页</button>
        </nav>
      </main>
    </div>

    <!-- 返回顶部（参考图中常驻显示） -->
    <button class="back-top" @click="scrollToTop" aria-label="返回顶部">
      <el-icon><Top /></el-icon>
      <span>返回顶部</span>
    </button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'
import SpotRankCard from '../components/SpotRankCard.vue'
import { getSpotRank } from '../api'

const route = useRoute()
const router = useRouter()

/* ---------- 页面配置（集中管理视觉参数） ---------- */
const showDecor = ref(true)          // 背景水墨装饰开关
const bannerImg = '/images/chongqing-banner.jpg'

const categories = [
  { key: 'all', label: '全部' },
  { key: 'hot', label: '热门活动榜' },
  { key: 'classic', label: '经典必打卡' },
  { key: 'nature', label: '自然风光' },
  { key: 'history', label: '人文历史' }
]

/* ---------- 列表状态 ---------- */
const spots = ref([])
const total = ref(0)
const category = ref('all')
const sort = ref('heat')
const keyword = ref('')
const page = ref(1)
// 首屏两行共 8 张卡片，与参考图排布一致
const pageSize = ref(8)
const loading = ref(false)

const totalPages = computed(() => Math.max(Math.ceil(total.value / pageSize.value), 1))

// 页码列表：数量少时全部展示
const pageList = computed(() => {
  const pages = []
  for (let i = 1; i <= totalPages.value; i++) pages.push(i)
  return pages
})

/* ---------- 数据加载 ---------- */
async function loadRank() {
  loading.value = true
  try {
    const data = await getSpotRank({
      category: category.value,
      sort: sort.value,
      keyword: keyword.value || undefined,
      page: page.value,
      size: pageSize.value
    })
    spots.value = data?.list || []
    total.value = data?.total || 0
    // 页码越界时回退到最后一页
    if (page.value > totalPages.value) {
      page.value = totalPages.value
      await loadRank()
    }
  } catch (e) {
    spots.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

function changeCategory(key) {
  if (category.value === key) return
  category.value = key
  page.value = 1
  loadRank()
}

function changeSort() {
  page.value = 1
  loadRank()
}

function goPage(p) {
  if (p < 1 || p > totalPages.value || p === page.value) return
  page.value = p
  loadRank()
}

function clearKeyword() {
  keyword.value = ''
  page.value = 1
  router.replace({ path: '/spots' })
  loadRank()
}

function resetAll() {
  category.value = 'all'
  sort.value = 'heat'
  keyword.value = ''
  page.value = 1
  router.replace({ path: '/spots' })
  loadRank()
}

/* ---------- 搜索关键词（来自顶部搜索框） ---------- */
watch(
  () => route.query.keyword,
  (val) => {
    keyword.value = (val || '').toString().trim()
    page.value = 1
    loadRank()
  }
)

/* ---------- 返回顶部 ---------- */
const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

onMounted(() => {
  keyword.value = (route.query.keyword || '').toString().trim()
  loadRank()
})
</script>

<style scoped>
/* ================= 设计变量（视觉参数集中管理） ================= */
.ranking-page {
  --rk-bg: #f7f1e7;
  --rk-card: #fffcf7;
  --rk-nav: #fffdf9;
  --rk-primary: #d95e36;
  --rk-primary-soft: #eca276;
  --rk-brown: #80624d;
  --rk-ink: #302920;
  --rk-muted: #9a8c7c;
  --rk-line: #e9dcc9;
  --rk-card-radius: 13px;
  --rk-cover-height: 158px;
  --rk-content-width: 1240px;
  --rk-col-gap: 18px;
  --rk-row-gap: 23px;
  --rk-dur: 260ms;

  min-height: 100vh;
  background: var(--rk-bg);
  color: var(--rk-ink);
  padding-bottom: 64px;
  overflow-x: hidden;
}

/* ================= 横幅 ================= */
.hero {
  position: relative;
  height: 245px;
  overflow: hidden;
}
.hero-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center 58%;
  display: block;
}
.hero-veil {
  position: absolute;
  inset: 0;
  background:
    linear-gradient(90deg, rgba(36, 24, 14, 0.66) 0%, rgba(36, 24, 14, 0.30) 46%, rgba(36, 24, 14, 0.10) 100%),
    linear-gradient(180deg, rgba(238, 173, 108, 0.22) 0%, rgba(247, 241, 231, 0) 62%);
}
.hero-inner {
  position: absolute;
  inset: 0;
  max-width: var(--rk-content-width);
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
}
.hero-copy {
  color: #fff;
  animation: rk-hero-in 0.7s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-eyebrow {
  display: block;
  font-size: 11px;
  letter-spacing: 4px;
  color: #f4d3ad;
  margin-bottom: 10px;
}
.hero-title {
  margin: 0 0 10px;
  font-family: var(--yj-font-serif);
  font-size: 40px;
  font-weight: 700;
  letter-spacing: 6px;
  text-shadow: 0 3px 18px rgba(30, 20, 12, 0.36);
}
.hero-sub {
  margin: 0;
  font-size: 13.5px;
  letter-spacing: 1.4px;
  color: rgba(255, 250, 243, 0.86);
}
.hero-poem {
  margin: 0;
  text-align: right;
  white-space: nowrap;
  font-family: var(--yj-font-serif);
  font-size: 14.5px;
  line-height: 2;
  letter-spacing: 4px;
  color: rgba(255, 248, 238, 0.72);
  animation: rk-hero-in 0.8s 0.1s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}

/* ================= 主体 ================= */
.page-body {
  position: relative;
}
.rank-main {
  position: relative;
  z-index: 2;
  max-width: var(--rk-content-width);
  margin: 0 auto;
  padding: 18px 20px 0;
}

/* ================= 筛选栏 ================= */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  height: 60px;
  padding: 0 14px;
  background: var(--rk-card);
  border: 1px solid var(--rk-line);
  border-radius: 14px;
  box-shadow: 0 6px 20px rgba(122, 74, 42, 0.07);
}
.list-entry {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  height: 34px;
  padding: 0 15px;
  border-radius: 9px;
  background: var(--rk-primary);
  color: #fff;
  font-size: 13.5px;
  font-weight: 600;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(217, 94, 54, 0.26);
  flex-shrink: 0;
}
.list-mark {
  width: 8px;
  height: 8px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.8);
  transform: rotate(45deg);
}
.filter-divider {
  width: 1px;
  height: 20px;
  background: var(--rk-line);
  flex-shrink: 0;
}
.filter-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex: 1;
  min-width: 0;
  overflow-x: auto;
  scrollbar-width: none;
}
.filter-tabs::-webkit-scrollbar { display: none; }
.filter-tab {
  border: none;
  background: transparent;
  padding: 7px 12px;
  border-radius: 8px;
  font-size: 13px;
  color: #5d4d3e;
  font-family: inherit;
  cursor: pointer;
  white-space: nowrap;
  transition: background var(--rk-dur) ease, color var(--rk-dur) ease;
}
.filter-tab:hover {
  color: var(--rk-primary);
  background: rgba(217, 94, 54, 0.06);
}
.filter-tab.active {
  color: var(--rk-primary);
  font-weight: 600;
  background: rgba(217, 94, 54, 0.09);
}
.filter-right {
  margin-left: auto;
  flex-shrink: 0;
}
.sort-select { width: 108px; }

/* 排序下拉：与筛选栏气质统一 */
.sort-select :deep(.el-select__wrapper) {
  box-shadow: none;
  background: transparent;
  font-size: 12.5px;
  color: var(--rk-brown);
  padding-left: 8px;
}
.sort-select :deep(.el-select__wrapper:hover) {
  background: rgba(217, 94, 54, 0.05);
}
.sort-select :deep(.el-select__placeholder) { color: var(--rk-brown); }

/* ================= 搜索提示 ================= */
.search-note {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: 16px 0 0;
  font-size: 12.5px;
  color: var(--rk-muted);
}
.search-note strong { color: var(--rk-brown); font-weight: 600; }
.clear-btn {
  border: none;
  background: none;
  color: var(--rk-primary);
  font-size: 12.5px;
  cursor: pointer;
  padding: 0 2px;
}
.clear-btn:hover { text-decoration: underline; }

/* ================= 卡片网格 ================= */
.card-grid {
  margin-top: 24px;
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  column-gap: var(--rk-col-gap);
  row-gap: var(--rk-row-gap);
  min-height: 120px;
}

/* ================= 空态 ================= */
.empty-wrap {
  padding: 56px 0 40px;
  text-align: center;
}
.empty-title {
  margin: 0 0 6px;
  font-size: 15px;
  color: var(--rk-brown);
}
.empty-tip {
  margin: 0 0 18px;
  font-size: 12.5px;
  color: var(--rk-muted);
}
.empty-btn {
  border: 1px solid rgba(217, 94, 54, 0.3);
  background: transparent;
  color: var(--rk-primary);
  font-size: 13px;
  padding: 7px 18px;
  border-radius: 20px;
  cursor: pointer;
  transition: background var(--rk-dur) ease;
}
.empty-btn:hover { background: rgba(217, 94, 54, 0.07); }

/* ================= 分页 ================= */
.pager {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  margin-top: 30px;
}
.pager button {
  min-width: 32px;
  height: 32px;
  padding: 0 10px;
  border-radius: 8px;
  border: 1px solid var(--rk-line);
  background: var(--rk-card);
  color: var(--rk-brown);
  font-size: 12.5px;
  font-family: inherit;
  cursor: pointer;
  transition: all var(--rk-dur) ease;
}
.pager button:hover:not(:disabled) {
  border-color: rgba(217, 94, 54, 0.4);
  color: var(--rk-primary);
}
.pager button.active {
  background: var(--rk-primary);
  border-color: var(--rk-primary);
  color: #fff;
}
.pager button:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

/* ================= 背景水墨装饰 ================= */
.decor {
  position: fixed;
  top: 300px;
  z-index: 1;
  color: #7a5c42;
  opacity: 0.2;
  pointer-events: none;
  user-select: none;
}
.decor-left {
  left: 1vw;
  width: 86px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}
.decor-right {
  right: 1vw;
  width: 110px;
  display: flex;
  justify-content: flex-end;
}
.decor-seal {
  width: 30px;
  padding: 5px 0;
  border: 1px solid currentColor;
  border-radius: 4px;
  font-family: var(--yj-font-serif);
  font-size: 13px;
  line-height: 1.15;
  letter-spacing: 1.5px;
  text-align: center;
  writing-mode: vertical-rl;
  opacity: 0.9;
}
.decor-verse {
  margin: 0;
  font-family: var(--yj-font-serif);
  font-size: 10.5px;
  line-height: 2;
  letter-spacing: 2px;
  writing-mode: vertical-rl;
  text-align: center;
  color: currentColor;
}
.decor-ink { width: 100%; height: auto; }
.decor-ink-right { max-width: 160px; }

/* ================= 返回顶部 ================= */
.back-top {
  position: fixed;
  right: 26px;
  bottom: 36px;
  z-index: 20;
  width: 54px;
  height: 54px;
  border-radius: 50%;
  border: 1px solid var(--rk-line);
  background: rgba(255, 252, 247, 0.94);
  color: var(--rk-primary);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 1px;
  font-size: 9.5px;
  font-family: inherit;
  cursor: pointer;
  box-shadow: 0 6px 18px rgba(122, 74, 42, 0.12);
  backdrop-filter: blur(6px);
  transition: transform var(--rk-dur) ease, box-shadow var(--rk-dur) ease;
}
.back-top .el-icon { font-size: 15px; }
.back-top:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 22px rgba(122, 74, 42, 0.16);
}

/* ================= 动效 ================= */
@keyframes rk-hero-in {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.rk-fade-enter-active, .rk-fade-leave-active { transition: opacity 0.2s ease; }
.rk-fade-enter-from, .rk-fade-leave-to { opacity: 0; }

/* ================= 响应式 ================= */
@media (max-width: 1300px) {
  .decor { opacity: 0.12; }
  .decor-left { width: 110px; left: 4px; }
  .decor-right { width: 120px; right: 4px; }
}
@media (max-width: 1180px) {
  .card-grid { grid-template-columns: repeat(4, minmax(0, 1fr)); }
}
@media (max-width: 1100px) {
  .decor { display: none; }
}
@media (max-width: 980px) {
  .card-grid { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .hero { height: 200px; }
  .hero-title { font-size: 34px; }
}
/* 平板（860 以下）两列，保证卡片与图片比例不至于过宽 */
@media (max-width: 860px) {
  .card-grid { grid-template-columns: repeat(2, minmax(0, 1fr)); }
}
@media (max-width: 760px) {
  .filter-bar {
    height: auto;
    padding: 10px 12px;
    flex-wrap: wrap;
    gap: 8px;
  }
  .filter-divider { display: none; }
  .filter-tabs {
    order: 3;
    flex-basis: 100%;
    gap: 2px;
    flex-wrap: wrap;
    overflow: visible;
  }
  .filter-right { margin-left: auto; }
  .sort-select { width: 100px; }
  .hero { height: 186px; }
  .hero-poem { display: none; }
  .hero-title { font-size: 30px; letter-spacing: 4px; }
  .hero-sub { font-size: 12px; }
}
@media (max-width: 520px) {
  .card-grid { grid-template-columns: 1fr; }
  /* 单列时保持图片接近 1.6:1，避免画面过扁 */
  .ranking-page { --rk-cover-height: 205px; }
  .hero-title { font-size: 26px; }
  .hero-eyebrow { font-size: 10px; letter-spacing: 3px; }
  .back-top { right: 14px; bottom: 20px; width: 46px; height: 46px; }
  .rank-main { padding: 14px 14px 0; }
  .hero-inner { padding: 0 14px; }
}

/* 尊重系统减少动效设置 */
@media (prefers-reduced-motion: reduce) {
  .hero-copy,
  .hero-poem { animation: none; }
  .ranking-page,
  .back-top,
  .filter-tab,
  .pager button { transition: none; }
}
</style>
