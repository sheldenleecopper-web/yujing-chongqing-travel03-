import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'
import router from '../router'

const request = axios.create({
  baseURL: '/api',
  timeout: 65000
})

request.interceptors.request.use(config => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})

request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      return res.data
    }
    ElMessage.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message))
  },
  error => {
    if (error.response && error.response.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      ElMessage.error('请先登录')
      router.push('/login')
    } else if (error.response && error.response.status === 403) {
      const userStore = useUserStore()
      userStore.logout()
      ElMessage.error('无管理员权限')
      router.push('/admin/login')
    } else {
      ElMessage.error(error.message || '网络异常')
    }
    return Promise.reject(error)
  }
)

export default request
