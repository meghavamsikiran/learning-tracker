import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '/api',
  headers: {
    'Content-Type': 'application/json',
  },
})

// ── Dashboard ──────────────────────────────────
export const fetchDashboard = () => api.get('/dashboard').then(r => r.data)
export const fetchHeatmap = () => api.get('/dashboard/heatmap').then(r => r.data)

// ── Curriculum ─────────────────────────────────
export const fetchCurriculum = () => api.get('/curriculum').then(r => r.data)
export const fetchPhase = (id) => api.get(`/curriculum/phases/${id}`).then(r => r.data)
export const updateTopic = (id, data) => api.put(`/curriculum/topics/${id}`, data).then(r => r.data)
export const completeTopic = (id, minutes) =>
  api.post(`/curriculum/topics/${id}/complete${minutes ? `?minutesSpent=${minutes}` : ''}`).then(r => r.data)

// ── Activities ─────────────────────────────────
export const fetchActivities = (limit = 50) => api.get(`/activities?limit=${limit}`).then(r => r.data)
export const fetchGroupedActivities = (limit = 100) => api.get(`/activities/grouped?limit=${limit}`).then(r => r.data)
export const logStudyTime = (data) => api.post('/activities/log', data).then(r => r.data)

// ── Stats ──────────────────────────────────────
export const fetchStats = () => api.get('/stats').then(r => r.data)
export const fetchStreaks = () => api.get('/stats/streaks').then(r => r.data)

export default api
