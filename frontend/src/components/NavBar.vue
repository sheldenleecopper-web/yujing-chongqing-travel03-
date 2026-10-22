<template>
  <div class="navbar">
    <div class="nav-inner">
      <div class="logo" @click="$router.push('/')">
        <div class="logo-mark">
          <el-icon :size="18" color="#fff"><Location /></el-icon>
        </div>
        <div class="logo-text">
          <span class="logo-name">渝境智旅</span>
          <span class="logo-sub">YUJING AI TRAVEL</span>
        </div>
      </div>

      <!-- 桌面端导航 -->
      <div class="nav-links">
        <router-link to="/" exact-active-class="active">首页</router-link>
        <router-link to="/spots" active-class="active">景点推荐</router-link>
        <router-link to="/food" active-class="active">重庆美食</router-link>
        <router-link to="/heritage" active-class="active">非遗文化</router-link>
        <router-link to="/profile" active-class="active">个人主页</router-link>
      </div>

      <!-- 搜索框 -->
      <div class="nav-search">
        <el-icon class="search-icon"><Search /></el-icon>
        <input
          v-model="keyword"
          class="search-input"
          placeholder="搜索景点、美食、非遗、攻略…"
          @keyup.enter="doSearch"
        />
      </div>

      <!-- 用户区 -->
      <div class="nav-user">
        <template v-if="userStore.token">
          <div class="user-chip">
            <span class="user-avatar">{{ userStore.username ? userStore.username.charAt(0).toUpperCase() : 'U' }}</span>
            <span class="username">{{ userStore.username }}</span>
          </div>
          <el-button text class="logout-btn" @click="handleLogout">退出</el-button>
        </template>
        <template v-else>
          <router-link to="/login">
            <el-button type="danger" round>登录 / 注册</el-button>
          </router-link>
        </template>
      </div>

      <!-- 移动端汉堡按钮 -->
      <el-icon class="hamburger" @click="mobileMenuOpen = !mobileMenuOpen">
        <Menu v-if="!mobileMenuOpen" />
        <Close v-else />
      </el-icon>
    </div>

    <!-- 移动端菜单 -->
    <transition name="fade">
      <div class="mobile-menu" v-if="mobileMenuOpen" @click.self="mobileMenuOpen = false">
        <div class="mobile-menu-inner" @click.stop>
          <router-link to="/" @click="mobileMenuOpen = false">首页</router-link>
          <router-link to="/spots" @click="mobileMenuOpen = false">景点推荐</router-link>
          <router-link to="/food" @click="mobileMenuOpen = false">重庆美食</router-link>
          <router-link to="/heritage" @click="mobileMenuOpen = false">非遗文化</router-link>
          <router-link to="/profile" @click="mobileMenuOpen = false">个人主页</router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'

const userStore = useUserStore()
const router = useRouter()
const keyword = ref('')
const mobileMenuOpen = ref(false)

const doSearch = () => {
  const kw = keyword.value.trim()
  if (!kw) return
  router.push({ path: '/search', query: { keyword: kw } })
}

const handleLogout = () => {
  userStore.logout()
  router.push('/')
}
</script>

<style scoped>
.navbar {
  height: 68px;
  background: rgba(251, 248, 241, 0.88);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-bottom: 1px solid rgba(191, 77, 46, 0.10);
  box-shadow: 0 2px 16px rgba(122, 74, 42, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
}
.nav-inner {
  max-width: 1320px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 32px;
  gap: 36px;
}

/* ---- Logo ---- */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  flex-shrink: 0;
}
.logo-mark {
  width: 38px;
  height: 38px;
  border-radius: 11px;
  background: linear-gradient(135deg, var(--yj-primary) 0%, var(--yj-primary-soft) 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(191, 77, 46, 0.32), inset 0 1px 0 rgba(255,255,255,0.25);
  transition: transform 0.3s ease;
}
.logo:hover .logo-mark { transform: rotate(-6deg) scale(1.05); }
.logo-text { display: flex; flex-direction: column; line-height: 1.15; }
.logo-name {
  font-family: var(--yj-font-serif);
  font-size: 20px;
  font-weight: 700;
  letter-spacing: 2px;
  color: var(--yj-primary-deep);
}
.logo-sub {
  font-size: 9px;
  letter-spacing: 2.5px;
  color: var(--yj-gold);
  font-weight: 600;
}

/* ---- 导航链接 ---- */
.nav-links {
  display: flex;
  gap: 6px;
  flex: 1;
}
.nav-links a {
  position: relative;
  text-decoration: none;
  color: var(--yj-ink-2);
  font-size: 15px;
  padding: 8px 14px;
  border-radius: 10px;
  transition: color 0.25s ease, background 0.25s ease;
}
.nav-links a:hover {
  color: var(--yj-primary);
  background: rgba(191, 77, 46, 0.07);
}
.nav-links a.active {
  color: var(--yj-primary);
  font-weight: 600;
}
.nav-links a.active::after {
  content: '';
  position: absolute;
  left: 14px;
  right: 14px;
  bottom: 2px;
  height: 3px;
  border-radius: 2px;
  background: linear-gradient(90deg, var(--yj-primary), var(--yj-gold));
}

/* ---- 搜索 ---- */
.nav-search {
  position: relative;
  width: 240px;
  flex-shrink: 0;
}
.search-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--yj-muted);
  font-size: 15px;
  z-index: 1;
}
.search-input {
  width: 100%;
  height: 40px;
  padding: 0 16px 0 38px;
  border: 1px solid var(--yj-line);
  border-radius: 20px;
  background: #fff;
  color: var(--yj-ink);
  font-size: 13px;
  outline: none;
  transition: border-color 0.25s, box-shadow 0.25s;
}
.search-input::placeholder { color: var(--yj-muted); }
.search-input:focus {
  border-color: var(--yj-primary-soft);
  box-shadow: 0 0 0 3px rgba(191, 77, 46, 0.12);
}

/* ---- 用户区 ---- */
.nav-user {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}
.user-chip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 4px 12px 4px 4px;
  background: #fff;
  border: 1px solid var(--yj-line);
  border-radius: 20px;
}
.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--yj-primary), var(--yj-primary-soft));
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}
.username {
  font-size: 13px;
  color: var(--yj-ink-2);
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.logout-btn { color: var(--yj-muted); }
.logout-btn:hover { color: var(--yj-primary); }

/* ---- 移动端 ---- */
.hamburger {
  display: none;
  font-size: 22px;
  color: var(--yj-ink-2);
  cursor: pointer;
  flex-shrink: 0;
}
.mobile-menu {
  position: fixed;
  inset: 68px 0 0 0;
  background: rgba(24, 32, 41, 0.4);
  backdrop-filter: blur(4px);
  z-index: 99;
  animation: yj-fade-in 0.2s ease both;
}
.mobile-menu-inner {
  background: var(--yj-bg-soft);
  padding: 10px 16px 16px;
  border-radius: 0 0 18px 18px;
  box-shadow: var(--yj-shadow-lg);
}
.mobile-menu-inner a {
  display: block;
  padding: 14px 16px;
  color: var(--yj-ink-2);
  text-decoration: none;
  font-size: 15px;
  border-radius: 12px;
  transition: background 0.2s, color 0.2s;
}
.mobile-menu-inner a:active,
.mobile-menu-inner a.router-link-active {
  color: var(--yj-primary);
  background: rgba(191, 77, 46, 0.08);
  font-weight: 600;
}
.fade-enter-active, .fade-leave-active { transition: opacity 0.2s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }

@media (max-width: 1024px) {
  .nav-inner { gap: 20px; padding: 0 20px; }
  .nav-search { width: 190px; }
}
@media (max-width: 900px) {
  .nav-links { display: none; }
  .nav-search { width: 160px; margin-left: auto; }
  .hamburger { display: block; }
}
@media (max-width: 600px) {
  .nav-search { display: none; }
  .username { max-width: 56px; }
  .logo-sub { display: none; }
}
</style>
