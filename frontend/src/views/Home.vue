<template>
  <div class="home-page">
    <NavBar />

    <!-- Hero Banner -->
    <section class="hero">
      <div class="hero-bg">
        <img :src="heroBg" alt="重庆城市风光" />
        <div class="hero-overlay"></div>
      </div>
      <div class="hero-content">
        <div class="hero-tag">山水之城 · 美丽之地</div>
        <h1 class="hero-title">重庆的艺术学</h1>
        <p class="hero-sub">山水之城 人文之地</p>
        <div class="hero-stats">
          <div class="hero-stat"><span class="stat-num">3000+</span><span class="stat-label">年巴渝文脉</span></div>
          <div class="stat-divider"></div>
          <div class="hero-stat"><span class="stat-num">8D</span><span class="stat-label">魔幻立体城市</span></div>
          <div class="stat-divider"></div>
          <div class="hero-stat"><span class="stat-num">两江</span><span class="stat-label">交汇灯火夜景</span></div>
        </div>
      </div>
    </section>

    <div class="page-wrap">
      <!-- 重庆印象 -->
      <section class="section impression yj-fade-up">
        <div class="sec-head">
          <span class="sec-dot"></span>
          <h2 class="sec-title">重庆印象</h2>
        </div>
        <div class="impression-body">
          <div class="impression-text">
            <div class="impression-quote">
              <p>山城重庆，依山傍水，因独特的地形与人文风情闻名。</p>
              <p>这里有壮丽的江河、有热闹的市井、有深厚的巴渝文化，</p>
              <p>是一座让人来了就不想走的城市。</p>
            </div>
            <div class="impression-icons">
              <div class="imp-item">
                <div class="imp-icon">🏙</div>
                <div><div class="imp-name">立体山城</div><div class="imp-desc">8D 魔幻地形</div></div>
              </div>
              <div class="imp-item">
                <div class="imp-icon">🌉</div>
                <div><div class="imp-name">桥都江景</div><div class="imp-desc">两江四岸灯火</div></div>
              </div>
              <div class="imp-item">
                <div class="imp-icon">🍲</div>
                <div><div class="imp-name">麻辣江湖</div><div class="imp-desc">火锅小面飘香</div></div>
              </div>
            </div>
          </div>
          <div class="impression-img">
            <el-carousel :interval="4000" arrow="hover" class="impression-carousel">
              <el-carousel-item v-for="(imgUrl, index) in impressionImgList" :key="index">
                <img :src="imgUrl" alt="重庆景点轮播图" class="carousel-img" />
              </el-carousel-item>
            </el-carousel>
          </div>
        </div>
      </section>

      <!-- 为你推荐 -->
      <section class="section recommend">
        <div class="sec-head">
          <span class="sec-dot"></span>
          <h2 class="sec-title">为你推荐</h2>
          <div class="sec-tabs">
            <span :class="['sec-tab', recommendTab === 'spot' && 'active']" @click="recommendTab = 'spot'">人气景点</span>
            <span :class="['sec-tab', recommendTab === 'hot' && 'active']" @click="recommendTab = 'hot'">热度榜单</span>
          </div>
          <span class="sec-more">查看更多 →</span>
        </div>
        <div class="rec-cards">
          <div class="rec-card" v-for="(spot, idx) in displaySpots" :key="idx">
            <div class="rec-cover">
              <img :src="spot.cover" :alt="spot.name" />
              <div class="rec-cover-mask"></div>
              <span class="rec-cover-tag">{{ spot.typeLabel }}</span>
            </div>
            <div class="rec-info">
              <div class="rec-name">{{ spot.name }}</div>
              <div class="rec-meta">
                <span class="rec-region"><el-icon><Location /></el-icon>{{ spot.region }}</span>
                <span class="rec-heat"><span class="rec-dot"></span>{{ spot.heatIndex || spot.heat }}热度</span>
              </div>
              <div class="rec-footer">
                <span class="rec-type">{{ spot.typeLabel }}</span>
                <span class="rec-detail">查看详情 →</span>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 重庆热度推荐榜 -->
      <section class="section ranking-section">
        <div class="ranking-header">
          <div class="ranking-title-wrap">
            <div class="sec-head" style="margin-bottom:6px">
              <span class="sec-dot"></span>
              <h2 class="sec-title" style="margin:0">重庆热度推荐榜</h2>
            </div>
            <p class="ranking-sub">基于游客浏览热度实时更新</p>
          </div>
          <span class="ranking-more">查看更多 →</span>
        </div>
        <div class="ranking-boards">
          <!-- 景点榜 -->
          <div class="board">
            <div class="board-head">
              <span class="board-icon attraction">景</span>
              <h3 class="board-name">景点榜</h3>
            </div>
            <div class="board-cards">
              <div class="rank-card" v-for="(item, idx) in attractionList" :key="'a'+idx">
                <div class="card-top">
                  <img :src="item.cover" :alt="item.name" class="card-img" />
                  <span class="rank-badge" :class="'rank-'+(idx+1)">TOP{{ idx+1 }}</span>
                  <el-icon class="fav-icon"><Star /></el-icon>
                </div>
                <div class="card-body">
                  <div class="card-name">{{ item.name }}</div>
                  <div class="card-region">{{ item.region }}</div>
                  <div class="card-tags">
                    <span class="tag tag-blue">{{ item.tag1 }}</span>
                    <span class="tag tag-green">{{ item.tag2 }}</span>
                  </div>
                  <div class="card-meta">
                    <span class="heat"><i class="dot"></i>热度 {{ item.heat }}</span>
                    <span class="price">¥{{ item.price }} 起</span>
                  </div>
                  <div class="card-reason">上榜理由：{{ item.reason }}</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 美食榜 -->
          <div class="board">
            <div class="board-head">
              <span class="board-icon food">食</span>
              <h3 class="board-name">美食榜</h3>
            </div>
            <div class="board-cards">
              <div class="rank-card" v-for="(item, idx) in foodList" :key="'f'+idx">
                <div class="card-top">
                  <img :src="item.cover" :alt="item.name" class="card-img" />
                  <span class="rank-badge" :class="'rank-'+(idx+1)">TOP{{ idx+1 }}</span>
                  <el-icon class="fav-icon"><Star /></el-icon>
                </div>
                <div class="card-body">
                  <div class="card-name">{{ item.name }}</div>
                  <div class="card-region">{{ item.region }}</div>
                  <div class="card-tags">
                    <span class="tag tag-orange">{{ item.tag1 }}</span>
                    <span class="tag tag-red">{{ item.tag2 }}</span>
                  </div>
                  <div class="card-meta">
                    <span class="heat"><i class="dot"></i>热度 {{ item.heat }}</span>
                    <span class="price">¥{{ item.price }} 起</span>
                  </div>
                  <div class="card-reason">上榜理由：{{ item.reason }}</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 酒店榜 -->
          <div class="board">
            <div class="board-head">
              <span class="board-icon hotel">宿</span>
              <h3 class="board-name">酒店榜</h3>
            </div>
            <div class="board-cards">
              <div class="rank-card" v-for="(item, idx) in hotelList" :key="'h'+idx">
                <div class="card-top">
                  <img :src="item.cover" :alt="item.name" class="card-img" />
                  <span class="rank-badge" :class="'rank-'+(idx+1)">TOP{{ idx+1 }}</span>
                  <el-icon class="fav-icon"><Star /></el-icon>
                </div>
                <div class="card-body">
                  <div class="card-name">{{ item.name }}</div>
                  <div class="card-region">{{ item.region }}</div>
                  <div class="card-tags">
                    <span class="tag tag-purple">{{ item.tag1 }}</span>
                    <span class="tag tag-blue">{{ item.tag2 }}</span>
                  </div>
                  <div class="card-meta">
                    <span class="heat"><i class="dot"></i>热度 {{ item.heat }}</span>
                    <span class="price">¥{{ item.price }} 起</span>
                  </div>
                  <div class="card-reason">上榜理由：{{ item.reason }}</div>
                </div>
              </div>
            </div>
          </div>
          <!-- 演艺活动榜 -->
          <div class="board">
            <div class="board-head">
              <span class="board-icon activity">演</span>
              <h3 class="board-name">演艺活动榜</h3>
            </div>
            <div class="board-cards">
              <div class="rank-card" v-for="(item, idx) in activityList" :key="'c'+idx">
                <div class="card-top">
                  <img :src="item.cover" :alt="item.name" class="card-img" />
                  <span class="rank-badge" :class="'rank-'+(idx+1)">TOP{{ idx+1 }}</span>
                  <el-icon class="fav-icon"><Star /></el-icon>
                </div>
                <div class="card-body">
                  <div class="card-name">{{ item.name }}</div>
                  <div class="card-region">{{ item.region }}</div>
                  <div class="card-tags">
                    <span class="tag tag-pink">{{ item.tag1 }}</span>
                    <span class="tag tag-orange">{{ item.tag2 }}</span>
                  </div>
                  <div class="card-meta">
                    <span class="heat"><i class="dot"></i>热度 {{ item.heat }}</span>
                    <span class="price">¥{{ item.price }} 起</span>
                  </div>
                  <div class="card-reason">上榜理由：{{ item.reason }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 抖音实时热榜 -->
      <section class="section news-section">
        <div class="sec-head">
          <span class="sec-dot"></span>
          <h2 class="sec-title">抖音实时热榜</h2>
          <span class="sec-sub">全网热点 · 点击跳转查看详情</span>
          <a class="sec-more" href="https://www.douyin.com/hot" target="_blank">完整热榜 →</a>
        </div>
        <div class="news-list">
          <a
            class="news-item"
            v-for="(news, idx) in douyinHotList"
            :key="idx"
            :href="'https://www.douyin.com/search/' + encodeURIComponent(news.title)"
            target="_blank"
            rel="noopener noreferrer"
          >
            <div class="news-rank" :class="'rank-'+(idx+1)">{{ idx+1 }}</div>
            <div class="news-content">
              <div class="news-title">{{ news.title }}</div>
              <div class="news-desc">{{ news.desc }}</div>
              <div class="news-meta">
                <span class="news-time">{{ news.time }}</span>
                <span class="news-heat"><span class="hot-dot"></span>{{ news.heat }} 热度</span>
              </div>
            </div>
            <el-tag size="small" type="danger" effect="light" class="news-tag">{{ news.tag }}</el-tag>
          </a>
        </div>
      </section>

      <!-- 智能生成攻略 CTA -->
      <section class="cta-section" style="padding:0;">
        <div class="cta-inner">
          <div class="cta-icon">🤖</div>
          <div class="cta-text">
            <div class="cta-title">智能生成攻略</div>
            <div class="cta-desc">告诉 AI 你的时间、兴趣与预算，生成专属重庆行程</div>
          </div>
          <el-button type="danger" size="large" class="cta-btn" @click="goForm">
            立即生成攻略 →
          </el-button>
        </div>
      </section>
    </div>

    <!-- 推荐路线 -->
    <section class="page-wrap">
      <div class="section routes" style="margin-top:48px;">
        <div class="sec-head">
          <span class="sec-dot"></span>
          <h2 class="sec-title">推荐路线</h2>
          <span class="sec-sub">精选行程 · 深度体验</span>
          <span class="sec-more">查看更多 →</span>
        </div>
        <div class="route-cards">
          <div class="route-card" v-for="(r, idx) in routeCards" :key="idx">
            <div class="route-cover">
              <img :src="r.cover" :alt="r.name" />
            </div>
            <div class="route-info">
              <div>
                <div class="route-name">{{ r.name }}</div>
                <div class="route-meta">
                  <el-icon><Clock /></el-icon>
                  <span>{{ r.days }}</span>
                  <span class="route-tags">{{ r.tags }}</span>
                </div>
              </div>
              <div class="route-btn">查看路线 →</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="site-footer">
      <div class="footer-inner">
        <div class="footer-brand">
          <span class="footer-logo">渝境智旅</span>
          <span class="footer-slogan">AI 带你读懂山城重庆</span>
        </div>
        <div class="footer-copy">渝境智旅 · 山水之城 美丽之地</div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'

const router = useRouter()

// 图片生成工具函数
const img = (seed) => `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=${encodeURIComponent(seed)}&image_size=landscape_16_9`

// 顶部主图
const heroBg = 'https://user46645.cn.imgto.link/public/20261020/3c068fc8cb8b21b071f09865-20261020214325-928-16.avif?__imgto_raw=1'

// 重庆印象轮播图
const impressionImgList = ref([
  'https://imgcdn.scol.com.cn/media/2026/04/24/112606210953.jpg',
  'https://imgcdn.scol.com.cn/media/2026/04/24/112606210953.jpg',
  'https://imgcdn.scol.com.cn/media/2026/04/24/112607362855.jpg',
  'https://imgcdn.scol.com.cn/media/2026/04/24/112611314489.jpg',
  'https://imgcdn.scol.com.cn/media/2026/04/24/112623626372.jpg'
])

// 为你推荐
const recommendTab = ref('spot')
const typeLabelMap = { attraction: '景点榜', food: '美食榜', activity: '活动榜' }
const recommendSpots = ref([
  { id: 1, name: '洪崖洞民俗风貌区', region: '渝中区 · 嘉陵江畔', heat: '98.6万', type: 'attraction', heatIndex: '98.6w', cover: img('重庆洪崖洞夜景吊脚楼') },
  { id: 2, name: '李子坝轻轨站', region: '渝中区 · 轻轨穿楼', heat: '76.3万', type: 'attraction', heatIndex: '76.3w', cover: img('重庆李子坝轻轨穿楼') },
  { id: 3, name: '长江索道', region: '渝中区 · 空中巴士', heat: '82.8万', type: 'attraction', heatIndex: '82.8w', cover: img('重庆长江索道过江') },
  { id: 4, name: '磁器口古镇', region: '沙坪坝区 · 千年古镇', heat: '54.2万', type: 'attraction', heatIndex: '54.2w', cover: img('重庆磁器口古镇街景') }
])
const hotSpots = ref([
  { id: 5, name: '武隆天生三桥', region: '武隆区 · 仙女山镇', heat: '87.2万', type: 'hot', heatIndex: '87.2w', cover: img('重庆武隆天生三桥自然风光') },
  { id: 6, name: '大足石刻', region: '大足区 · 世界遗产', heat: '68.9万', type: 'hot', heatIndex: '68.9w', cover: img('重庆大足石刻') },
  { id: 7, name: '南山一棵树', region: '南岸区 · 夜景最佳', heat: '72.4万', type: 'hot', heatIndex: '72.4w', cover: img('重庆南山一棵树夜景') },
  { id: 8, name: '鹅岭二厂', region: '渝中区 · 文创公园', heat: '61.7万', type: 'hot', heatIndex: '61.7w', cover: img('重庆鹅岭二厂文创园') }
])
const displaySpots = computed(() => {
  const list = recommendTab.value === 'spot' ? recommendSpots.value : hotSpots.value
  return list.slice(0, 4).map(s => ({ ...s, typeLabel: typeLabelMap[s.type] || '人气榜' }))
})

// 抖音热榜数据
const douyinHotList = ref([
  { title: '2026年国庆档票房破亿影片名单', desc: '多部国产大片扎堆上映，国庆档票房竞争激烈，多部影片首日票房破亿', time: '实时上升', heat: '986.5万', tag: '热榜第一' },
  { title: '年轻人反向旅游成新趋势', desc: '避开热门景区，选择小众县城与冷门目的地，低成本高质量旅行走红', time: '2小时前', heat: '752.3万', tag: '社会' },
  { title: '最新科技产品发布会全程回顾', desc: '多款旗舰新品集中亮相，配置价格全汇总，一文看懂所有亮点', time: '4小时前', heat: '628.9万', tag: '科技' },
  { title: '秋季养生饮食指南走红', desc: '入秋后饮食调理要点，润肺去燥食谱大全，网友亲测有效', time: '6小时前', heat: '514.7万', tag: '生活' },
  { title: '全国多地降温迎来初秋天', desc: '冷空气南下多地气温骤降，秋季穿搭攻略登上热搜', time: '昨日', heat: '426.1万', tag: '天气' }
])

// 推荐路线
const routeCards = ref([
  { name: '山城经典一日游', days: '约 6 小时', tags: '洪崖洞 · 解放碑 · 长江索道', cover: img('重庆洪崖洞解放碑长江索道地标景点') },
  { name: '巴渝人文两日游', days: '约 2 天', tags: '磁器口 · 三峡博物馆 · 白公馆', cover: img('重庆磁器口古镇鹅岭二厂白公馆人文景点') },
  { name: '江岸夜景漫游', days: '约 4 小时', tags: '洪崖洞 · 长江大桥 · 两江夜景', cover: img('重庆两江交汇夜景南山一棵树航拍') }
])

// 四大榜单数据
const attractionList = ref([
  { name: '洪崖洞民俗风貌区', region: '渝中区 · 嘉陵江畔', tag1: '夜景', tag2: '民俗', heat: '98.6万', price: '0', reason: '山城夜景地标，吊脚楼群灯火璀璨', cover: img('重庆洪崖洞夜景吊脚楼') },
  { name: '武隆天生三桥', region: '武隆区 · 仙女山镇', tag1: '自然奇观', tag2: '5A景区', heat: '87.2万', price: '125', reason: '世界自然遗产，喀斯特地貌震撼', cover: img('重庆武隆天生三桥自然风光') },
  { name: '磁器口古镇', region: '沙坪坝区 · 磁器口街道', tag1: '古镇', tag2: '巴渝文化', heat: '76.5万', price: '0', reason: '千年古镇，石板路与老茶馆韵味悠长', cover: img('重庆磁器口古镇街景') }
])
const foodList = ref([
  { name: '重庆老火锅', region: '渝中区 · 解放碑', tag1: '麻辣', tag2: '必吃', heat: '95.3万', price: '88', reason: '牛油锅底麻辣鲜香，山城味道代表', cover: img('重庆老火锅麻辣牛油锅底') },
  { name: '重庆小面', region: '全市各区', tag1: '早餐', tag2: '面食', heat: '82.1万', price: '12', reason: '豌杂面、肥肠面，一碗唤醒山城清晨', cover: img('重庆小面豌杂面') },
  { name: '辣子鸡', region: '歌乐山 · 沙坪坝', tag1: '江湖菜', tag2: '香辣', heat: '68.7万', price: '68', reason: '歌乐山辣子鸡，辣椒堆里找鸡肉', cover: img('重庆歌乐山辣子鸡江湖菜') }
])
const hotelList = ref([
  { name: '重庆解放碑威斯汀酒店', region: '渝中区 · 解放碑', tag1: '五星', tag2: '江景', heat: '91.2万', price: '888', reason: '解放碑核心地段，俯瞰两江夜景', cover: img('重庆解放碑五星级酒店江景房') },
  { name: '重庆丽晶酒店', region: '江北区 · 江北嘴', tag1: '奢华', tag2: '江景', heat: '79.8万', price: '1288', reason: '两江交汇之上，奢华江景体验', cover: img('重庆江北嘴奢华酒店江景') },
  { name: '磁器口花间堂民宿', region: '沙坪坝区 · 磁器口', tag1: '民宿', tag2: '古镇', heat: '65.4万', price: '368', reason: '古镇内庭院民宿，闹中取静', cover: img('重庆磁器口古镇庭院民宿') }
])
const activityList = ref([
  { name: '印象·武隆实景演出', region: '武隆区 · 仙女山', tag1: '实景演出', tag2: '非遗', heat: '89.5万', price: '180', reason: '张艺谋导演，川江号子震撼人心', cover: img('重庆武隆印象实景演出') },
  { name: '重庆国际马戏城', region: '南岸区 · 南滨路', tag1: '马戏', tag2: '亲子', heat: '72.3万', price: '120', reason: '国际级马戏表演，亲子首选', cover: img('重庆马戏城表演现场') },
  { name: '国泰艺术中心话剧', region: '渝中区 · 解放碑', tag1: '话剧', tag2: '文艺', heat: '58.9万', price: '180', reason: '山城文化地标，精品话剧常年上演', cover: img('重庆国泰艺术中心话剧演出') }
])

// 按钮事件
const goForm = () => {
  router.push('/guide/form')
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  padding-bottom: 0;
}

/* ---- Hero ---- */
.hero {
  position: relative;
  height: 480px;
  overflow: hidden;
}
.hero-bg { position: absolute; inset: 0; }
.hero-bg img { width: 100%; height: 100%; object-fit: cover; transform: scale(1.02); }
.hero-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, rgba(24, 32, 41, 0.15) 0%, rgba(24, 32, 41, 0.30) 55%, rgba(24, 32, 41, 0.72) 100%);
}
.hero-content {
  position: relative; z-index: 2;
  height: 100%; max-width: 1200px;
  margin: 0 auto; padding: 96px 24px 0;
  color: #fff;
  animation: yj-fade-up 0.7s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-tag {
  display: inline-block;
  padding: 6px 16px;
  background: rgba(255, 255, 255, 0.16);
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.28);
  border-radius: 20px;
  font-size: 12px;
  letter-spacing: 3px;
  margin-bottom: 22px;
}
.hero-title {
  font-family: var(--yj-font-serif);
  font-size: 46px; font-weight: 700;
  margin: 0 0 12px; letter-spacing: 4px;
  text-shadow: 0 4px 24px rgba(0, 0, 0, 0.35);
}
.hero-sub {
  font-size: 17px; opacity: 0.92; margin: 0 0 34px;
  letter-spacing: 6px;
}
.hero-stats {
  display: inline-flex;
  align-items: center;
  gap: 22px;
  padding: 14px 26px;
  background: rgba(24, 32, 41, 0.38);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.16);
  border-radius: 14px;
}
.hero-stat { display: flex; flex-direction: column; gap: 2px; }
.stat-num {
  font-size: 20px; font-weight: 700;
  color: #ffd9a8;
  font-family: var(--yj-font-serif);
}
.stat-label { font-size: 12px; color: rgba(255, 255, 255, 0.78); }
.stat-divider { width: 1px; height: 30px; background: rgba(255, 255, 255, 0.22); }

/* ---- 通用版心与小节头 ---- */
.page-wrap { max-width: 1200px; margin: 0 auto; padding: 0 24px; }
.section { margin-top: 52px; }
.sec-head {
  display: flex; align-items: center; gap: 10px;
  margin-bottom: 22px;
}
.sec-dot {
  width: 4px; height: 20px;
  background: linear-gradient(180deg, var(--yj-primary), var(--yj-gold));
  border-radius: 2px;
}
.sec-title {
  font-family: var(--yj-font-serif);
  font-size: 22px; font-weight: 700;
  color: var(--yj-ink); margin: 0;
  letter-spacing: 1px;
}
.sec-sub { font-size: 13px; color: var(--yj-muted); margin-left: 4px; }
.sec-more {
  margin-left: auto;
  color: var(--yj-muted); text-decoration: none; font-size: 13px;
  cursor: pointer;
  transition: color 0.25s;
}
.sec-more:hover { color: var(--yj-primary); }
.sec-tabs { display: flex; gap: 18px; margin-left: 10px; }
.sec-tab {
  font-size: 13px; color: var(--yj-muted); cursor: pointer;
  padding: 4px 2px; border-bottom: 2px solid transparent;
  transition: all 0.25s;
}
.sec-tab:hover { color: var(--yj-primary); }
.sec-tab.active {
  color: var(--yj-primary); border-bottom-color: var(--yj-primary); font-weight: 600;
}

/* ---- 重庆印象 ---- */
.impression-body {
  display: grid;
  grid-template-columns: 1fr 1.1fr;
  gap: 36px; align-items: center;
}
.impression-quote {
  background: var(--yj-card);
  border: 1px solid var(--yj-line);
  border-left: 4px solid var(--yj-primary);
  padding: 22px 24px;
  border-radius: var(--yj-radius-sm);
  box-shadow: var(--yj-shadow-sm);
}
.impression-quote p {
  font-size: 15px; color: var(--yj-ink-2);
  line-height: 2.0; margin: 0;
  font-family: var(--yj-font-serif);
}
.impression-icons { display: flex; gap: 22px; margin-top: 24px; flex-wrap: wrap; }
.imp-item {
  display: flex; align-items: center; gap: 10px;
  background: var(--yj-card);
  border: 1px solid var(--yj-line);
  padding: 10px 16px 10px 10px;
  border-radius: 12px;
  box-shadow: var(--yj-shadow-sm);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.imp-item:hover { transform: translateY(-3px); box-shadow: var(--yj-shadow); }
.imp-icon {
  width: 40px; height: 40px;
  background: linear-gradient(135deg, #fbe6d8, #f7d3ba);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 18px;
}
.imp-name { font-size: 14px; font-weight: 600; color: var(--yj-ink); }
.imp-desc { font-size: 12px; color: var(--yj-muted); margin-top: 2px; }
.impression-img {
  border-radius: var(--yj-radius); overflow: hidden;
  box-shadow: var(--yj-shadow);
  height: 280px;
  background: var(--yj-line);
  position: relative;
}
.impression-carousel {
  width: 100%;
  height: 280px !important;
}
.impression-carousel :deep(.el-carousel__container) {
  height: 280px !important;
  border-radius: var(--yj-radius);
  overflow: hidden;
}
.impression-carousel :deep(.el-carousel__item) {
  height: 280px !important;
}
.impression-carousel :deep(.el-carousel__indicators) { bottom: 10px; }
.carousel-img {
  width: 100%;
  height: 280px;
  object-fit: cover;
  display: block;
}

/* ---- 为你推荐 ---- */
.rec-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 18px;
}
.rec-card {
  background: var(--yj-card); border-radius: var(--yj-radius); overflow: hidden;
  cursor: pointer;
  border: 1px solid var(--yj-line);
  transition: transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1), box-shadow 0.35s ease;
  box-shadow: var(--yj-shadow-sm);
}
.rec-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--yj-shadow-lg);
}
.rec-cover { height: 150px; overflow: hidden; position: relative; }
.rec-cover img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.6s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.rec-card:hover .rec-cover img { transform: scale(1.08); }
.rec-cover-mask {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, transparent 55%, rgba(24, 32, 41, 0.4) 100%);
}
.rec-cover-tag {
  position: absolute; left: 10px; bottom: 10px;
  font-size: 11px; color: #fff;
  background: rgba(191, 77, 46, 0.85);
  backdrop-filter: blur(4px);
  padding: 3px 10px; border-radius: 20px;
  letter-spacing: 0.5px;
}
.rec-info { padding: 13px 15px 15px; }
.rec-name {
  font-size: 15px; font-weight: 600; color: var(--yj-ink);
  margin-bottom: 7px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  transition: color 0.25s;
}
.rec-card:hover .rec-name { color: var(--yj-primary); }
.rec-meta {
  display: flex; justify-content: space-between; align-items: center;
  font-size: 12px; color: var(--yj-muted); margin-bottom: 10px;
}
.rec-region {
  display: flex; align-items: center; gap: 3px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.rec-heat {
  color: var(--yj-primary); display: flex; align-items: center; gap: 4px;
  flex-shrink: 0;
}
.rec-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: var(--yj-primary); display: inline-block;
  box-shadow: 0 0 5px rgba(191, 77, 46, 0.6);
}
.rec-footer {
  display: flex; justify-content: space-between; align-items: center;
  padding-top: 10px; border-top: 1px dashed var(--yj-line);
}
.rec-type {
  font-size: 12px; color: var(--yj-primary);
  background: rgba(191, 77, 46, 0.08); padding: 3px 9px; border-radius: 6px;
}
.rec-detail {
  font-size: 12px; color: var(--yj-muted);
  transition: color 0.25s, transform 0.25s;
}
.rec-card:hover .rec-detail { color: var(--yj-primary); transform: translateX(2px); }

/* ---- 热度推荐榜 ---- */
.ranking-section {
  background: var(--yj-card); border-radius: var(--yj-radius);
  padding: 26px 30px 30px;
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow-sm);
}
.ranking-header {
  display: flex; justify-content: space-between; align-items: flex-end;
  margin-bottom: 22px; padding-bottom: 16px;
  border-bottom: 1px solid var(--yj-line);
}
.ranking-sub { font-size: 13px; color: var(--yj-muted); margin: 0 0 0 14px; }
.ranking-more {
  color: var(--yj-muted); text-decoration: none; font-size: 13px; cursor: pointer;
  transition: color 0.25s;
}
.ranking-more:hover { color: var(--yj-primary); }
.ranking-boards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 18px;
}
.board {
  background: var(--yj-bg-soft); border-radius: var(--yj-radius-sm);
  padding: 16px; border: 1px solid var(--yj-line);
}
.board-head { display: flex; align-items: center; gap: 9px; margin-bottom: 14px; }
.board-icon {
  width: 30px; height: 30px; border-radius: 9px;
  display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 14px; font-weight: 600;
  font-family: var(--yj-font-serif);
  box-shadow: 0 3px 8px rgba(122, 74, 42, 0.18);
}
.board-icon.attraction { background: linear-gradient(135deg, #bf4d2e, #d9764a); }
.board-icon.food { background: linear-gradient(135deg, #e07835, #f0a060); }
.board-icon.hotel { background: linear-gradient(135deg, #a67c52, #c9a67e); }
.board-icon.activity { background: linear-gradient(135deg, #b85c5c, #d88080); }
.board-name {
  font-size: 15px; font-weight: 700; color: var(--yj-ink); margin: 0;
  font-family: var(--yj-font-serif);
  letter-spacing: 1px;
}
.board-cards { display: flex; flex-direction: column; gap: 12px; }
.rank-card {
  background: var(--yj-card); border-radius: var(--yj-radius-sm); overflow: hidden;
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow-sm);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}
.rank-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--yj-shadow);
}
.card-top { position: relative; height: 104px; overflow: hidden; }
.card-img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.5s ease;
}
.rank-card:hover .card-img { transform: scale(1.06); }
.rank-badge {
  position: absolute; top: 8px; left: 8px;
  background: rgba(24, 32, 41, 0.6);
  backdrop-filter: blur(4px);
  color: #fff; font-size: 10px; font-weight: 700;
  padding: 3px 8px; border-radius: 6px; letter-spacing: 0.5px;
}
.rank-badge.rank-1 { background: linear-gradient(135deg, #f5a623, #ffd54f); color: #5d4037; }
.rank-badge.rank-2 { background: linear-gradient(135deg, #b0a090, #d8ccc0); color: #3e2723; }
.rank-badge.rank-3 { background: linear-gradient(135deg, #a1887f, #d7ccc8); color: #3e2723; }
.fav-icon {
  position: absolute; top: 8px; right: 8px;
  color: #fff; font-size: 16px;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.4); cursor: pointer;
  transition: transform 0.25s;
}
.fav-icon:hover { transform: scale(1.2); color: #ffd54f; }
.card-body { padding: 9px 11px 11px; }
.card-name {
  font-size: 13px; font-weight: 600; color: var(--yj-ink);
  margin-bottom: 3px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.card-region {
  font-size: 11px; color: var(--yj-muted); margin-bottom: 7px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.card-tags { display: flex; gap: 5px; margin-bottom: 7px; flex-wrap: wrap; }
.tag { font-size: 10px; padding: 2px 7px; border-radius: 5px; font-weight: 500; }
.tag-blue { background: rgba(191, 77, 46, 0.09); color: var(--yj-primary); }
.tag-green { background: #f0f5e8; color: #6b8e23; }
.tag-orange { background: #fff3e0; color: #e07835; }
.tag-red { background: #fbe9e7; color: #d84315; }
.tag-purple { background: #f3e5dc; color: #8d6e63; }
.tag-pink { background: #fce4ec; color: #c2185b; }
.card-meta {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 7px;
}
.heat {
  font-size: 11px; color: var(--yj-primary);
  display: flex; align-items: center; gap: 4px;
}
.heat .dot {
  width: 5px; height: 5px; border-radius: 50%;
  background: var(--yj-primary); display: inline-block;
}
.price { font-size: 12px; color: var(--yj-primary); font-weight: 700; }
.card-reason {
  font-size: 10px; color: var(--yj-muted); line-height: 1.6;
  border-top: 1px dashed var(--yj-line); padding-top: 6px;
}

/* ---- 抖音热榜 ---- */
.news-list {
  background: var(--yj-card);
  border-radius: var(--yj-radius);
  padding: 8px 24px;
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow-sm);
}
.news-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 17px 0;
  border-bottom: 1px solid var(--yj-line);
  transition: background-color 0.25s;
  text-decoration: none;
  color: inherit;
}
.news-item:last-child { border-bottom: none; }
.news-item:hover { background-color: var(--yj-bg-soft); margin: 0 -24px; padding: 17px 24px; }
.news-rank {
  width: 30px; height: 30px; border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 14px; font-weight: 700;
  color: var(--yj-muted); background: var(--yj-bg); flex-shrink: 0;
  font-family: var(--yj-font-serif);
}
.news-rank.rank-1 { background: linear-gradient(135deg, #f5a623, #ffd54f); color: #5d4037; }
.news-rank.rank-2 { background: linear-gradient(135deg, #b0a090, #d8ccc0); color: #3e2723; }
.news-rank.rank-3 { background: linear-gradient(135deg, #a1887f, #d7ccc8); color: #3e2723; }
.news-content { flex: 1; min-width: 0; }
.news-title {
  font-size: 15px; font-weight: 600; color: var(--yj-ink);
  margin-bottom: 6px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  transition: color 0.25s;
}
.news-item:hover .news-title { color: var(--yj-primary); }
.news-desc {
  font-size: 12px; color: var(--yj-ink-2); line-height: 1.6; margin-bottom: 8px;
  display: -webkit-box; -webkit-line-clamp: 1; -webkit-box-orient: vertical; overflow: hidden;
}
.news-meta {
  display: flex; justify-content: space-between; align-items: center;
  font-size: 12px; color: var(--yj-muted);
}
.news-heat { color: #fe2c55; display: flex; align-items: center; gap: 4px; font-weight: 500; }
.news-tag { flex-shrink: 0; }
.hot-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: #fe2c55; display: inline-block;
  box-shadow: 0 0 5px rgba(254, 44, 85, 0.55);
}

/* ---- CTA ---- */
.cta-section {
  margin: 56px auto 0;
  max-width: 1152px;
  padding: 0 24px;
}
.cta-inner {
  display: flex; align-items: center; gap: 20px;
  background: linear-gradient(120deg, #2a2320 0%, #4a2e1e 55%, #7a3b1e 100%);
  border-radius: var(--yj-radius); padding: 28px 34px;
  box-shadow: var(--yj-shadow-lg);
  position: relative;
  overflow: hidden;
}
.cta-inner::after {
  content: '';
  position: absolute;
  right: -60px; top: -60px;
  width: 220px; height: 220px;
  border-radius: 50%;
  background: radial-gradient(circle, rgba(217, 118, 74, 0.35) 0%, transparent 70%);
  pointer-events: none;
}
.cta-icon {
  font-size: 30px; width: 56px; height: 56px;
  background: rgba(255, 255, 255, 0.12);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
}
.cta-text { flex: 1; position: relative; z-index: 1; }
.cta-title {
  font-size: 18px; font-weight: 700; color: #fff; margin-bottom: 5px;
  font-family: var(--yj-font-serif);
  letter-spacing: 1px;
}
.cta-desc { font-size: 13px; color: rgba(255, 255, 255, 0.72); }
.cta-btn { position: relative; z-index: 1; }

/* ---- 推荐路线 ---- */
.route-cards {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
}
.route-card {
  background: var(--yj-card); border-radius: var(--yj-radius); overflow: hidden;
  cursor: pointer;
  border: 1px solid var(--yj-line);
  box-shadow: var(--yj-shadow-sm);
  transition: transform 0.35s cubic-bezier(0.22, 0.61, 0.36, 1), box-shadow 0.35s ease;
  display: flex;
  height: 128px;
}
.route-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--yj-shadow-lg);
}
.route-cover { width: 150px; flex-shrink: 0; overflow: hidden; }
.route-cover img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.6s ease;
}
.route-card:hover .route-cover img { transform: scale(1.08); }
.route-info {
  padding: 15px 17px; flex: 1;
  display: flex; flex-direction: column;
  justify-content: space-between;
  min-width: 0;
}
.route-name {
  font-size: 15px; font-weight: 600; color: var(--yj-ink); margin-bottom: 7px;
  font-family: var(--yj-font-serif);
}
.route-meta {
  display: flex; align-items: center; gap: 6px;
  font-size: 12px; color: var(--yj-muted); flex-wrap: wrap;
}
.route-tags { color: var(--yj-ink-2); }
.route-btn {
  font-size: 13px; color: var(--yj-primary); font-weight: 500;
  transition: transform 0.25s;
}
.route-card:hover .route-btn { transform: translateX(3px); }

/* ---- 页脚 ---- */
.site-footer {
  margin-top: 64px;
  background: var(--yj-night);
  color: rgba(255, 255, 255, 0.65);
}
.footer-inner {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 12px;
}
.footer-brand { display: flex; align-items: baseline; gap: 12px; }
.footer-logo {
  font-family: var(--yj-font-serif);
  font-size: 18px; font-weight: 700;
  color: #e8b48a;
  letter-spacing: 2px;
}
.footer-slogan { font-size: 12px; color: rgba(255, 255, 255, 0.45); }
.footer-copy { font-size: 12px; color: rgba(255, 255, 255, 0.4); }

@media (max-width: 1200px) {
  .rec-cards { grid-template-columns: repeat(2, 1fr); }
  .ranking-boards { grid-template-columns: repeat(2, 1fr); }
  .route-cards { grid-template-columns: 1fr; }
}
@media (max-width: 900px) {
  .hero { height: 400px; }
  .hero-content { padding: 70px 24px 0; }
  .hero-title { font-size: 32px; }
  .hero-stats { gap: 14px; padding: 12px 18px; }
  .impression-body { grid-template-columns: 1fr; }
  .rec-cards { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 600px) {
  .rec-cards { grid-template-columns: 1fr; }
  .ranking-boards { grid-template-columns: 1fr; }
  .impression-icons { flex-direction: column; gap: 12px; }
  .cta-inner { flex-wrap: wrap; padding: 22px; }
  .route-card { height: auto; flex-direction: column; }
  .route-cover { width: 100%; height: 150px; }
  .news-item { flex-wrap: wrap; }
  .news-tag { width: 100%; margin-top: 4px; }
  .hero-stats { display: none; }
}
</style>
