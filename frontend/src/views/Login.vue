<template>
  <div class="login-page">
    <div class="login-shell">
      <!-- 品牌视觉区 -->
      <div class="brand-pane">
        <img
          class="brand-img"
          src="https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=%E9%87%8D%E5%BA%86%E6%B4%AA%E5%B4%96%E6%B4%9E%E5%A4%9C%E6%99%AF&image_size=portrait_4_3"
          alt="重庆夜景"
        />
        <div class="brand-mask"></div>
        <div class="brand-text">
          <div class="brand-logo">渝境智旅</div>
          <div class="brand-slogan">山水之城 · 美丽之地</div>
          <div class="brand-desc">AI 智能行程规划，带你读懂山城重庆</div>
        </div>
      </div>

      <!-- 表单区 -->
      <div class="login-box">
        <h2>{{ isLogin ? '登录' : '注册' }} · 渝境智旅</h2>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="0"
          @submit.prevent="submit"
        >
          <el-form-item prop="username">
            <el-input v-model="form.username" placeholder="用户名（3-20字符）" prefix-icon="User" size="large" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="密码（6-20位）"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-form-item v-if="!isLogin" prop="confirmPassword">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="确认密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-button type="danger" class="submit-btn" size="large" @click="submit" :loading="loading">
            {{ isLogin ? '登录' : '注册' }}
          </el-button>
        </el-form>
        <div class="switch" @click="toggleMode">
          {{ isLogin ? '没有账号？去注册' : '已有账号？去登录' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const isLogin = ref(true)
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (!isLogin.value && value !== form.password) {
    callback(new Error('两次密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名需3-20个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码需6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const toggleMode = () => {
  isLogin.value = !isLogin.value
  formRef.value?.clearValidate()
}

const submit = async () => {
  try {
    await formRef.value.validate()
  } catch (e) {
    return
  }

  loading.value = true
  try {
    if (isLogin.value) {
      await userStore.login({ username: form.username, password: form.password })
      ElMessage.success('登录成功')
    } else {
      await userStore.register({ username: form.username, password: form.password })
      ElMessage.success('注册成功')
    }
    router.push('/')
  } catch (e) {
    // 错误已由 request 拦截器统一提示
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--yj-bg);
  padding: 24px;
}
.login-shell {
  display: flex;
  width: 860px;
  max-width: 100%;
  min-height: 540px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: var(--yj-shadow-lg);
  border: 1px solid var(--yj-line);
  background: var(--yj-card);
  animation: yj-fade-up 0.6s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}

/* ---- 品牌视觉区 ---- */
.brand-pane {
  flex: 1;
  position: relative;
  overflow: hidden;
  display: none;
}
@media (min-width: 720px) {
  .brand-pane { display: block; }
}
.brand-img {
  position: absolute;
  inset: 0;
  width: 100%; height: 100%;
  object-fit: cover;
}
.brand-mask {
  position: absolute;
  inset: 0;
  background: linear-gradient(200deg, rgba(24, 32, 41, 0.1) 0%, rgba(24, 32, 41, 0.72) 100%);
}
.brand-text {
  position: absolute;
  left: 32px; right: 32px; bottom: 36px;
  color: #fff;
  z-index: 2;
}
.brand-logo {
  font-family: var(--yj-font-serif);
  font-size: 32px;
  font-weight: 700;
  letter-spacing: 4px;
  margin-bottom: 8px;
  text-shadow: 0 3px 16px rgba(0, 0, 0, 0.4);
}
.brand-slogan {
  font-size: 14px;
  letter-spacing: 3px;
  color: #ffd9a8;
  margin-bottom: 10px;
}
.brand-desc {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.75);
  letter-spacing: 1px;
}

/* ---- 表单区 ---- */
.login-box {
  width: 420px;
  max-width: 100%;
  padding: 48px 44px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
}
.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: var(--yj-ink);
  font-family: var(--yj-font-serif);
  letter-spacing: 2px;
}
.submit-btn { width: 100%; letter-spacing: 4px; }
.switch {
  text-align: center;
  margin-top: 18px;
  color: var(--yj-primary);
  cursor: pointer;
  font-size: 14px;
  transition: color 0.25s;
}
.switch:hover { color: var(--yj-primary-deep); }

@media (max-width: 720px) {
  .login-box { width: 100%; padding: 40px 28px; }
}
</style>
