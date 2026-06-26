import { Routes, Route } from 'react-router-dom'
import { useState, useEffect } from 'react'
import Sidebar from './components/layout/Sidebar'
import { ToastProvider } from './components/common/ToastProvider'
import Dashboard from './pages/Dashboard'
import Curriculum from './pages/Curriculum'
import ActivityLog from './pages/ActivityLog'
import Statistics from './pages/Statistics'
import { fetchDashboard } from './api/client'

export default function App() {
  const [sidebarStats, setSidebarStats] = useState(null)

  useEffect(() => {
    fetchDashboard()
      .then(data => {
        setSidebarStats({
          currentStreak: data.currentStreak,
          totalTopicsCompleted: data.totalTopicsCompleted,
          totalTopics: data.totalTopics,
          totalXp: data.totalXp,
          level: data.level,
        })
      })
      .catch(() => {
        // Backend not available yet — sidebar will just not show stats
      })
  }, [])

  // Refresh sidebar stats when pages make changes
  function refreshStats() {
    fetchDashboard()
      .then(data => {
        setSidebarStats({
          currentStreak: data.currentStreak,
          totalTopicsCompleted: data.totalTopicsCompleted,
          totalTopics: data.totalTopics,
          totalXp: data.totalXp,
          level: data.level,
        })
      })
      .catch(() => {})
  }

  return (
    <ToastProvider>
      <div className="app-layout">
        <Sidebar stats={sidebarStats} />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/curriculum" element={<Curriculum />} />
            <Route path="/activity" element={<ActivityLog />} />
            <Route path="/stats" element={<Statistics />} />
          </Routes>
        </main>
      </div>
    </ToastProvider>
  )
}
