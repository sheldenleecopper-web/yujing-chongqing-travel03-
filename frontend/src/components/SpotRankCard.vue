<template>
  <article
    class="rank-card"
    :style="{ '--enter-index': Math.min(index, 9) }"
    @click="goDetail"
    @keydown.enter="goDetail"
    tabindex="0"
    role="link"
    :aria-label="`${spot.name}，排名第 ${rank}`"
  >
    <div class="cover">
      <img :src="spot.cover" :alt="spot.name" loading="lazy" />
      <div class="cover-shade"></div>

      <!-- 排名标记：第一名使用暖金色，其余为低调半透明 -->
      <div class="rank-badge" :class="{ 'is-first': rank === 1 }">
        <span class="rank-num">{{ rank }}</span>
        <span class="rank-top">TOP {{ rank }}</span>
      </div>

      <!-- 热度：取自后端真实热度值 -->
      <span class="heat-badge">
        <span class="heat-dot"></span>
        热度 {{ spot.heatIndex }}
      </span>
    </div>

    <div class="body">
      <h3 class="name" :title="spot.name">{{ spot.name }}</h3>
      <p class="region">
        <el-icon><Location /></el-icon>
        <span>{{ spot.region || '重庆' }}</span>
      </p>
      <p class="intro">{{ spot.intro }}</p>

      <div class="foot">
        <span class="price">{{ priceText }}</span>
        <span class="detail-link">
          查看详情
          <span class="arrow">→</span>
        </span>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  spot: { type: Object, required: true },
  rank: { type: Number, default: 1 },
  index: { type: Number, default: 0 }
})

const router = useRouter()

// 价格仅按后端返回的价格状态展示，未知不显示为免费
const priceText = computed(() => {
  const { priceType, avgCost } = props.spot
  if (priceType === 'unknown' || avgCost === null || avgCost === undefined) return '价格待确认'
  if (priceType === 'free' || Number(avgCost) === 0) return '免费'
  return `¥${Number(avgCost)} 起`
})

const goDetail = () => router.push(`/spot/${props.spot.id}`)
</script>

<style scoped>
/* 卡片样式集中在推荐页设计变量中，便于统一调整 */
.rank-card {
  background: var(--rk-card);
  border: 1px solid var(--rk-line);
  border-radius: var(--rk-card-radius);
  overflow: hidden;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  box-shadow: 0 2px 10px rgba(122, 74, 42, 0.05);
  transition: transform var(--rk-dur) ease, box-shadow var(--rk-dur) ease, border-color var(--rk-dur) ease;
  animation: rk-card-in 0.5s cubic-bezier(0.22, 0.61, 0.36, 1) both;
  animation-delay: calc(var(--enter-index) * 45ms);
}
.rank-card:hover,
.rank-card:focus-visible {
  transform: translateY(-3px);
  box-shadow: 0 12px 24px rgba(122, 74, 42, 0.11);
  border-color: rgba(217, 94, 54, 0.22);
  outline: none;
}

.cover {
  position: relative;
  height: var(--rk-cover-height);
  overflow: hidden;
  background: #efe4d4;
}
.cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.3s ease;
}
.rank-card:hover .cover img { transform: scale(1.03); }
.cover-shade {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(35, 24, 14, 0.26) 0%, rgba(35, 24, 14, 0) 40%, rgba(35, 24, 14, 0.2) 100%);
  pointer-events: none;
}

/* 排名 */
.rank-badge {
  position: absolute;
  top: 8px;
  left: 8px;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 2px 6px 2px 2px;
  border-radius: 6px;
  background: rgba(48, 41, 32, 0.34);
  backdrop-filter: blur(4px);
  color: #fff;
}
.rank-num {
  min-width: 17px;
  height: 17px;
  padding: 0 3px;
  border-radius: 4px;
  background: rgba(255, 255, 255, 0.9);
  color: #80624d;
  font-size: 11px;
  font-weight: 700;
  line-height: 17px;
  text-align: center;
}
.rank-top {
  font-size: 9px;
  letter-spacing: 0.6px;
  color: rgba(255, 255, 255, 0.92);
}
.rank-badge.is-first { background: rgba(140, 92, 26, 0.62); }
.rank-badge.is-first .rank-num {
  background: linear-gradient(135deg, #e3b166, #c98f3c);
  color: #fff;
}

/* 热度 */
.heat-badge {
  position: absolute;
  right: 8px;
  bottom: 8px;
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 7px;
  border-radius: 20px;
  background: rgba(38, 30, 22, 0.42);
  color: rgba(255, 244, 232, 0.94);
  font-size: 10.5px;
  letter-spacing: 0.2px;
  backdrop-filter: blur(4px);
}
.heat-dot {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #f0b083;
}

/* 文字区 */
.body {
  padding: 12px 13px 14px;
  display: flex;
  flex-direction: column;
  flex: 1;
}
.name {
  margin: 0;
  font-size: 15.5px;
  font-weight: 600;
  letter-spacing: 0.3px;
  color: var(--rk-ink);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color var(--rk-dur) ease;
}
.rank-card:hover .name { color: var(--rk-primary); }
.region {
  display: flex;
  align-items: center;
  gap: 3px;
  margin: 5px 0 7px;
  font-size: 11.5px;
  color: var(--rk-muted);
}
.region .el-icon { font-size: 12px; }
.intro {
  margin: 0;
  font-size: 12px;
  line-height: 1.68;
  color: #8f7d6a;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 41px;
}
.foot {
  margin-top: auto;
  padding-top: 10px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}
.price {
  font-size: 11.5px;
  color: var(--rk-brown);
}
.detail-link {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 11.5px;
  color: var(--rk-primary);
  transition: gap var(--rk-dur) ease;
}
.arrow { font-size: 12px; }
.rank-card:hover .detail-link { gap: 6px; }

@keyframes rk-card-in {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
@media (prefers-reduced-motion: reduce) {
  .rank-card,
  .cover img,
  .detail-link { animation: none !important; transition: none !important; }
  .rank-card:hover { transform: none; }
  .rank-card:hover .cover img { transform: none; }
}
</style>
