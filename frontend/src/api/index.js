import request from '../utils/request'

// 用户
export const register = (data) => request.post('/user/register', data)
export const login = (data) => request.post('/user/login', data)
export const getUserInfo = () => request.get('/user/info')

// 去处
export const getSpotList = (params) => request.get('/spot/list', { params })
export const getSpotDetail = (id) => request.get(`/spot/${id}`)
export const searchSpots = (keyword) => request.get('/spot/search', { params: { keyword } })

// 非遗
export const getHeritageList = (params) => request.get('/heritage/list', { params })
export const getHeritageDetail = (id) => request.get(`/heritage/${id}`)
export const searchHeritage = (keyword) => request.get('/heritage/search', { params: { keyword } })

// 收藏
export const addFavorite = (spotId) => request.post('/favorite/add', { spotId })
export const removeFavorite = (spotId) => request.post('/favorite/remove', { spotId })
export const getFavoriteList = () => request.get('/favorite/list')

// 浏览记录
export const getBrowseHistory = () => request.get('/history/browse')
export const deleteBrowseHistory = (spotId) => request.delete(`/history/browse/${spotId}`)
export const clearBrowseHistory = () => request.delete('/history/browse')

// 攻略
export const generateGuide = (data) => request.post('/guide/generate', data)
export const getGuideDetail = (id) => request.get(`/guide/${id}`)
export const saveGuide = (data) => request.post('/guide/save', data)
export const savePoster = (id, posterUrl) => request.post(`/guide/${id}/poster`, { posterUrl })
export const getGuideList = () => request.get('/guide/list')
export const getPosterRecordList = () => request.get('/guide/poster/list')
export const deleteGuide = (id) => request.delete(`/guide/${id}`)

// 推荐
export const getHomeRecommend = () => request.get('/recommend/home')

// 后台管理
export const adminLogin = (data) => request.post('/admin/login', data)
export const getAdminStats = () => request.get('/admin/stats')

// 去处管理
export const getAdminSpotList = () => request.get('/admin/spot/list')
export const addAdminSpot = (data) => request.post('/admin/spot', data)
export const updateAdminSpot = (id, data) => request.put(`/admin/spot/${id}`, data)
export const deleteAdminSpot = (id) => request.delete(`/admin/spot/${id}`)

// 非遗管理
export const getAdminHeritageList = () => request.get('/admin/heritage/list')
export const addAdminHeritage = (data) => request.post('/admin/heritage', data)
export const updateAdminHeritage = (id, data) => request.put(`/admin/heritage/${id}`, data)
export const deleteAdminHeritage = (id) => request.delete(`/admin/heritage/${id}`)

// 酒店管理
export const getAdminHotelList = () => request.get('/admin/hotel/list')
export const addAdminHotel = (data) => request.post('/admin/hotel', data)
export const updateAdminHotel = (id, data) => request.put(`/admin/hotel/${id}`, data)
export const deleteAdminHotel = (id) => request.delete(`/admin/hotel/${id}`)

// 用户管理
export const getAdminUserList = () => request.get('/admin/user/list')
