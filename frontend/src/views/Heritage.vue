<template>
  <div class="page-wrap">
    <NavBar />

    <!-- 页头横幅 -->
    <section class="page-hero">
      <img
        class="page-hero-img"
        src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=%E5%B7%9D%E5%89%A7%E5%8F%98%E8%84%B8%E9%9D%9E%E9%81%97%E6%96%87%E5%8C%96&image_size=landscape_16_9"
        alt="川剧变脸"
      />
      <div class="page-hero-mask"></div>
      <div class="page-hero-content">
        <span class="page-hero-tag">INTANGIBLE HERITAGE</span>
        <h2 class="page-hero-title">非遗文化</h2>
        <p class="page-hero-sub">巴渝文脉 · 匠心传承千年技艺</p>
      </div>
    </section>

    <div class="container">
      <div class="filter-bar">
        <el-radio-group v-model="category" @change="loadList">
          <el-radio-button label="">全部</el-radio-button>
          <el-radio-button label="传统音乐">传统音乐</el-radio-button>
          <el-radio-button label="传统舞蹈">传统舞蹈</el-radio-button>
          <el-radio-button label="传统戏剧">传统戏剧</el-radio-button>
          <el-radio-button label="传统美术">传统美术</el-radio-button>
          <el-radio-button label="传统技艺">传统技艺</el-radio-button>
          <el-radio-button label="民俗">民俗</el-radio-button>
        </el-radio-group>
      </div>

      <el-row :gutter="18" class="h-grid">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="h in list" :key="h.id">
          <el-card class="h-card" shadow="never" @click="showDetail(h)">
            <div class="h-cover">
              <img :src="h.cover" :alt="h.name" />
              <div class="h-cover-mask"></div>
              <span class="h-level" :class="{ national: h.level === '国家级' }">{{ h.level }}</span>
            </div>
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

      <el-dialog v-model="detailVisible" :title="current?.name" width="600px" class="h-dialog">
        <div v-if="current">
          <div class="dialog-cover">
            <img :src="current.cover" />
          </div>
          <p class="dialog-intro">{{ current.intro }}</p>
          <div class="dialog-meta">
            <p><strong>传承人：</strong>{{ current.inheritor || '暂无' }}</p>
            <p><strong>体验地点：</strong>{{ current.experienceSpot || '暂无' }}</p>
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import NavBar from '../components/NavBar.vue'
import { getHeritageList } from '../api'

const category = ref('')
const list = ref([])
const detailVisible = ref(false)
const current = ref(null)

onMounted(loadList)

async function loadList() {
  try {
    list.value = await getHeritageList({ category: category.value || undefined })
  } catch (e) {}
}

const showDetail = (h) => {
  current.value = h
  detailVisible.value = true
}
</script>

<style scoped>
.page-wrap { background: var(--yj-bg); min-height: 100vh; padding-bottom: 56px; }

/* ---- 页头横幅 ---- */
.page-hero {
  position: relative;
  height: 220px;
  overflow: hidden;
}
.page-hero-img {
  width: 100%; height: 100%;
  object-fit: cover;
  display: block;
}
.page-hero-mask {
  position: absolute; inset: 0;
  background: linear-gradient(90deg, rgba(24, 32, 41, 0.72) 0%, rgba(24, 32, 41, 0.35) 55%, rgba(24, 32, 41, 0.15) 100%);
}
.page-hero-content {
  position: absolute;
  inset: 0;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  color: #fff;
  animation: yj-fade-up 0.6s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.page-hero-tag {
  font-size: 11px;
  letter-spacing: 3px;
  color: #ffd9a8;
  margin-bottom: 8px;
  font-weight: 600;
}
.page-hero-title {
  font-family: var(--yj-font-serif);
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 3px;
  margin: 0 0 8px;
  text-shadow: 0 3px 16px rgba(0, 0, 0, 0.35);
}
.page-hero-sub {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 1px;
  margin: 0;
}

/* ---- 内容区 ---- */
.container { max-width: 1200px; margin: 0 auto; padding: 26px 20px 0; }
.filter-bar {
  margin: -34px 0 24px;
  position: relative;
  z-index: 2;
  background: var(--yj-card);
  padding: 16px 18px;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow);
  overflow-x: auto;
}
.h-grid { row-gap: 18px; }

/* ---- 非遗卡片 ---- */
.h-card {
  cursor: pointer;
  border-radius: var(--yj-radius);
  overflow: hidden;
  border: 1px solid var(--yj-line);
  transition: transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1), box-shadow 0.35s ease;
}
.h-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--yj-shadow-lg);
}
.h-card :deep(.el-card__body) { padding: 0; }
.h-cover { height: 186px; overflow: hidden; position: relative; }
.h-cover img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.h-card:hover .h-cover img { transform: scale(1.08); }
.h-cover-mask {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, rgba(24, 32, 41, 0.06) 50%, rgba(24, 32, 41, 0.42) 100%);
}
.h-level {
  position: absolute;
  left: 10px; bottom: 10px;
  font-size: 11px;
  padding: 3px 10px;
  border-radius: 20px;
  color: #fff;
  background: rgba(160, 110, 40, 0.85);
  backdrop-filter: blur(4px);
  letter-spacing: 1px;
}
.h-level.national { background: rgba(191, 77, 46, 0.88); }
.h-body { padding: 14px 14px 16px; }
.h-name {
  font-size: 16px; font-weight: 700; margin-bottom: 9px;
  color: var(--yj-ink);
  font-family: var(--yj-font-serif);
  transition: color 0.25s;
}
.h-card:hover .h-name { color: var(--yj-primary); }
.h-tags { display: flex; gap: 6px; margin-bottom: 9px; }
.h-inheritor { color: var(--yj-muted); font-size: 13px; }

/* ---- 详情弹窗 ---- */
.dialog-cover {
  border-radius: var(--yj-radius-sm);
  overflow: hidden;
  margin-bottom: 16px;
  box-shadow: var(--yj-shadow-sm);
}
.dialog-cover img { width: 100%; display: block; }
.dialog-intro {
  line-height: 1.9;
  color: var(--yj-ink-2);
  font-size: 14px;
  background: var(--yj-bg-soft);
  border: 1px solid var(--yj-line);
  border-left: 4px solid var(--yj-primary);
  border-radius: var(--yj-radius-sm);
  padding: 14px 16px;
}
.dialog-meta {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px dashed var(--yj-line);
  color: var(--yj-ink-2);
  font-size: 14px;
  line-height: 1.9;
}
.dialog-meta p { margin: 0; }
.dialog-meta strong { color: var(--yj-ink); }
.h-dialog :deep(.el-dialog__title) {
  font-family: var(--yj-font-serif);
  font-weight: 700;
  letter-spacing: 1px;
}

@media (max-width: 600px) {
  .page-hero { height: 170px; }
  .page-hero-title { font-size: 24px; }
  .filter-bar { margin-top: -26px; }
}
</style>
