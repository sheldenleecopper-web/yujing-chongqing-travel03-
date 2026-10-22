<template>
  <div class="admin-login">
    <el-card class="login-card">
      <h2 class="title">渝境智旅 · 管理后台</h2>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="管理员账号" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" prefix-icon="Lock" size="large" show-password @keyup.enter="handleLogin" />
        </el-form-item>
        <el-button type="primary" size="large" style="width:100%" :loading="loading" @click="handleLogin">登 录</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { adminLogin } from '../../api'
import { useUserStore } from '../../store/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const form = ref({ username: '', password: '' })
const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const handleLogin = async () => {
  await formRef.value.validate(async valid => {
    if (!valid) return
    loading.value = true
    try {
      const data = await adminLogin(form.value)
      userStore.setLogin(data)
      ElMessage.success('登录成功')
      router.push('/admin')
    } catch (e) {
      // 错误已由拦截器处理
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.admin-login {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-card {
  width: 380px;
  padding: 20px;
}
.title {
  text-align: center;
  margin-bottom: 24px;
  color: #303133;
}
</style>
