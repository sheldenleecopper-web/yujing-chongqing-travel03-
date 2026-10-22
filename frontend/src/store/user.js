import { defineStore } from 'pinia'
import { login as loginApi, register as registerApi } from '../api'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userId: localStorage.getItem('userId') || '',
    username: localStorage.getItem('username') || '',
    role: localStorage.getItem('role') || ''
  }),
  getters: {
    isLogin: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },
  actions: {
    setLogin(data) {
      this.token = data.token
      this.userId = data.userId
      this.username = data.username
      this.role = data.role || ''
      localStorage.setItem('token', data.token)
      localStorage.setItem('userId', data.userId)
      localStorage.setItem('username', data.username)
      localStorage.setItem('role', data.role || '')
    },
    async login(form) {
      const data = await loginApi(form)
      this.setLogin(data)
      return data
    },
    async register(form) {
      const data = await registerApi(form)
      this.setLogin(data)
      return data
    },
    logout() {
      this.token = ''
      this.userId = ''
      this.username = ''
      this.role = ''
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('username')
      localStorage.removeItem('role')
    }
  }
})
