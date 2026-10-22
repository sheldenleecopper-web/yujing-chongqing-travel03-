<template>
  <div class="page-wrap">
    <NavBar />

    <!-- 页头横幅 -->
    <section class="page-hero">
      <img
        class="page-hero-img"
        src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=%E9%87%8D%E5%BA%86%E6%B8%9D%E4%B8%AD%E5%8D%8A%E5%B2%9B%E5%A4%A9%E9%99%85%E7%BA%BF%E5%A4%9C%E6%99%AF%E6%B4%AA%E5%B4%96%E6%B4%9E%E7%81%AF%E7%81%AB&image_size=landscape_16_9"
        alt="重庆天际线"
      />
      <div class="page-hero-mask"></div>
      <div class="page-hero-content">
        <span class="page-hero-tag">SPOT RANKING</span>
        <h2 class="page-hero-title">景点推荐</h2>
        <p class="page-hero-sub">山水重庆 · 精选热度榜单实时更新</p>
      </div>
    </section>

    <div class="container">
      <div class="filter-bar">
        <el-radio-group v-model="activeTab" @change="loadList">
          <el-radio-button label="attraction">景点榜</el-radio-button>
          <el-radio-button label="food">美食榜</el-radio-button>
          <el-radio-button label="activity">快闪活动榜</el-radio-button>
        </el-radio-group>
        <el-select v-model="region" placeholder="区域" @change="loadList" style="width:140px">
          <el-option label="全部" value="全部" />
          <el-option v-for="r in regions" :key="r" :label="r" :value="r" />
        </el-select>
        <el-select v-model="sort" placeholder="排序" @change="loadList" style="width:140px">
          <el-option label="热度优先" value="热度优先" />
          <el-option label="评分优先" value="评分优先" />
          <el-option label="最新发布" value="最新发布" />
        </el-select>
        <el-button type="danger" @click="goGenerate" v-if="selectedStore.spotIds.length" class="gen-btn">
          生成攻略 ({{ selectedStore.spotIds.length }})
        </el-button>
      </div>

      <el-row :gutter="18" class="spot-grid">
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="spot in spots" :key="spot.id">
          <SpotCard :spot="spot" @select="addToSelected" @favorite="toggleFavorite" />
        </el-col>
      </el-row>

      <el-empty v-if="!spots.length" description="暂无数据" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import NavBar from '../components/NavBar.vue'
import SpotCard from '../components/SpotCard.vue'
import { getSpotList, addFavorite } from '../api'
import { useSelectedStore } from '../store/selected'
import { useUserStore } from '../store/user'

const router = useRouter()
const selectedStore = useSelectedStore()
const userStore = useUserStore()

const activeTab = ref('attraction')
const region = ref('全部')
const sort = ref('热度优先')
const spots = ref([])

const regions = ['渝中区', '江北区', '南岸区', '沙坪坝区', '九龙坡区', '渝北区', '巴南区', '北碚区', '大渡口区', '武隆区', '大足区', '巫山县', '酉阳县', '秀山县', '丰都县', '涪陵区', '万州区']

onMounted(loadList)

async function loadList() {
  try {
    spots.value = await getSpotList({ type: activeTab.value, region: region.value, sort: sort.value })
  } catch (e) {}
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

const goGenerate = () => {
  router.push('/guide/form')
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
  display: flex;
  align-items: center;
  gap: 14px;
  margin: -34px 0 24px;
  position: relative;
  z-index: 2;
  background: var(--yj-card);
  padding: 16px 18px;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow);
  flex-wrap: wrap;
}
.gen-btn { margin-left: auto; }
.spot-grid { row-gap: 18px; }

@media (max-width: 600px) {
  .page-hero { height: 170px; }
  .page-hero-title { font-size: 24px; }
  .filter-bar { margin-top: -26px; }
}
</style>
