<template>
  <el-card class="spot-card" shadow="never" @click="goDetail">
    <div class="cover">
      <img :src="spot.cover" :alt="spot.name" />
      <div class="cover-mask"></div>
      <div class="tags">
        <el-tag v-for="tag in tagList" :key="tag" size="small" type="danger" effect="dark">{{ tag }}</el-tag>
      </div>
      <div class="cover-heat">
        <span class="heat-dot"></span>热度 {{ spot.heatIndex }}
      </div>
    </div>
    <div class="info">
      <div class="name">{{ spot.name }}</div>
      <div class="region">
        <el-icon><Location /></el-icon>
        <span>{{ spot.region }}</span>
      </div>
      <div class="footer">
        <span class="heat">热度 {{ spot.heatIndex }}</span>
        <span v-if="spot.avgCost > 0" class="cost">¥{{ spot.avgCost }}</span>
      </div>
      <div class="actions" @click.stop>
        <el-button size="small" type="primary" plain @click="$emit('select', spot)">加入行程</el-button>
        <el-button size="small" type="danger" plain @click="$emit('favorite', spot)">{{ isFav ? '已收藏' : '收藏' }}</el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  spot: { type: Object, required: true },
  isFav: { type: Boolean, default: false }
})
defineEmits(['select', 'favorite'])

const router = useRouter()
const tagList = computed(() => (props.spot.tags || '').split(',').filter(Boolean))

const goDetail = () => {
  router.push(`/spot/${props.spot.id}`)
}
</script>

<style scoped>
.spot-card {
  cursor: pointer;
  overflow: hidden;
  border-radius: var(--yj-radius);
  border: 1px solid var(--yj-line);
  transition: transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1), box-shadow 0.35s ease, border-color 0.35s ease;
}
.spot-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--yj-shadow-lg);
  border-color: rgba(191, 77, 46, 0.25);
}
.spot-card :deep(.el-card__body) { padding: 0; }

.cover {
  height: 176px;
  position: relative;
  overflow: hidden;
}
.cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.spot-card:hover .cover img { transform: scale(1.08); }
.cover-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(180deg, rgba(24, 32, 41, 0.05) 40%, rgba(24, 32, 41, 0.45) 100%);
  pointer-events: none;
}
.tags {
  position: absolute;
  top: 10px;
  left: 10px;
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}
.tags :deep(.el-tag) {
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.18);
}
.cover-heat {
  position: absolute;
  right: 10px;
  bottom: 10px;
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 3px 10px;
  border-radius: 20px;
  background: rgba(24, 32, 41, 0.55);
  backdrop-filter: blur(6px);
  color: #ffd9c2;
  font-size: 12px;
}
.heat-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #ff8c5a;
  box-shadow: 0 0 6px #ff8c5a;
}

.info { padding: 14px 14px 16px; }
.name {
  font-size: 16px;
  font-weight: 600;
  color: var(--yj-ink);
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  transition: color 0.25s;
}
.spot-card:hover .name { color: var(--yj-primary); }
.region {
  display: flex;
  align-items: center;
  gap: 4px;
  color: var(--yj-muted);
  font-size: 13px;
  margin-bottom: 10px;
}
.footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  color: var(--yj-muted);
  margin-bottom: 12px;
  padding-top: 10px;
  border-top: 1px dashed var(--yj-line);
}
.heat { color: var(--yj-primary); font-weight: 500; }
.cost { color: var(--yj-gold); font-weight: 600; }
.actions {
  display: flex;
  gap: 8px;
}
.actions .el-button { flex: 1; }
</style>
