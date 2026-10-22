import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../store/user'

const routes = [
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/spots', component: () => import('../views/Ranking.vue') },
  { path: '/food', component: () => import('../views/Food.vue') },
  { path: '/heritage', component: () => import('../views/Heritage.vue') },
  { path: '/ranking', redirect: '/spots' },
  { path: '/search', component: () => import('../views/SearchResults.vue') },
  { path: '/spot/:id', component: () => import('../views/SpotDetail.vue') },
  { path: '/guide/form', component: () => import('../views/GuideForm.vue'), meta: { requiresAuth: true } },
  { path: '/guide/detail/:id', component: () => import('../views/GuideDetail.vue'), meta: { requiresAuth: true } },
  { path: '/history', redirect: '/profile' },
  { path: '/profile', component: () => import('../views/Profile.vue'), meta: { requiresAuth: true } },
  { path: '/login', component: () => import('../views/Login.vue') },
  // 后台管理
  { path: '/admin/login', component: () => import('../views/admin/AdminLogin.vue') },
  {
    path: '/admin',
    component: () => import('../views/admin/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'spot', component: () => import('../views/admin/SpotManage.vue') },
      { path: 'heritage', component: () => import('../views/admin/HeritageManage.vue') },
      { path: 'hotel', component: () => import('../views/admin/HotelManage.vue') },
      { path: 'user', component: () => import('../views/admin/UserManage.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  if (to.meta.requiresAdmin) {
    if (!userStore.token) {
      next('/admin/login')
    } else if (!userStore.isAdmin) {
      next('/admin/login')
    } else {
      next()
    }
  } else if (to.meta.requiresAuth && !userStore.token) {
    next('/login')
  } else {
    next()
  }
})

export default router
