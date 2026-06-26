import { useState, useEffect } from 'react'
import { fetchDashboard, fetchHeatmap } from '../api/client'
import Heatmap from '../components/dashboard/Heatmap'
import ProgressRing from '../components/common/ProgressRing'

export default function Dashboard() {
  const [dashboard, setDashboard] = useState(null)
  const [heatmapData, setHeatmapData] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([fetchDashboard(), fetchHeatmap()])
      .then(([dash, heatmap]) => {
        setDashboard(dash)
        setHeatmapData(heatmap)
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return (
      <div className="loading">
        <div className="loading-spinner" />
      </div>
    )
  }

  if (!dashboard) {
    return (
      <div className="empty-state">
        <span className="empty-state-icon">🔌</span>
        <h3 className="empty-state-title">Cannot connect to backend</h3>
        <p className="empty-state-text">Make sure the Spring Boot server is running on port 8080.</p>
      </div>
    )
  }

  const totalHours = Math.round(dashboard.totalStudyMinutes / 60)

  function getActivityIcon(type) {
    switch (type) {
      case 'TOPIC_COMPLETED': return { icon: '✅', cls: 'completed' }
      case 'TOPIC_STARTED': return { icon: '▶️', cls: 'started' }
      case 'STUDY_LOGGED': return { icon: '⏱️', cls: 'logged' }
      case 'PHASE_COMPLETED': return { icon: '🏆', cls: 'phase' }
      case 'NOTE_ADDED': return { icon: '📝', cls: 'logged' }
      default: return { icon: '📌', cls: 'logged' }
    }
  }

  function formatTimeAgo(dateStr) {
    const date = new Date(dateStr)
    const now = new Date()
    const diffMs = now - date
    const diffMins = Math.floor(diffMs / 60000)
    if (diffMins < 1) return 'just now'
    if (diffMins < 60) return `${diffMins}m ago`
    const diffHours = Math.floor(diffMins / 60)
    if (diffHours < 24) return `${diffHours}h ago`
    const diffDays = Math.floor(diffHours / 24)
    if (diffDays === 1) return 'yesterday'
    return `${diffDays}d ago`
  }

  return (
    <div className="animate-fade-in">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Dashboard</h1>
        <p className="page-subtitle">Your Java Backend Mentorship journey at a glance</p>
      </div>

      {/* Stats Grid */}
      <div className="dashboard-stats-grid stagger-children">
        <div className="stat-card dashboard-stat-card streak animate-fade-in-up">
          <span className="stat-card-icon animate-streak-fire">🔥</span>
          <span className="stat-card-value">{dashboard.currentStreak}</span>
          <span className="stat-card-label">Day Streak (Best: {dashboard.longestStreak})</span>
        </div>

        <div className="stat-card dashboard-stat-card topics animate-fade-in-up">
          <span className="stat-card-icon">✅</span>
          <span className="stat-card-value">
            {dashboard.totalTopicsCompleted}
            <span style={{ fontSize: 'var(--font-base)', color: 'var(--text-muted)', fontWeight: 400 }}>
              /{dashboard.totalTopics}
            </span>
          </span>
          <span className="stat-card-label">Topics Completed</span>
        </div>

        <div className="stat-card dashboard-stat-card xp animate-fade-in-up">
          <span className="stat-card-icon">⚡</span>
          <span className="stat-card-value">{dashboard.totalXp?.toLocaleString()}</span>
          <span className="stat-card-label">XP Earned (Level {dashboard.level})</span>
        </div>

        <div className="stat-card dashboard-stat-card time animate-fade-in-up">
          <span className="stat-card-icon">⏱️</span>
          <span className="stat-card-value">{totalHours}h</span>
          <span className="stat-card-label">Total Study Time</span>
        </div>
      </div>

      {/* Overall Progress Bar */}
      <div className="card" style={{ marginBottom: 'var(--space-6)' }}>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 'var(--space-3)' }}>
          <span className="card-subtitle" style={{ margin: 0 }}>Overall Progress</span>
          <span style={{ fontSize: 'var(--font-sm)', color: 'var(--accent-green)', fontWeight: 600 }}>
            {dashboard.completionPercentage}%
          </span>
        </div>
        <div className="progress-bar">
          <div className="progress-bar-fill" style={{ width: `${dashboard.completionPercentage}%` }} />
        </div>
      </div>

      {/* Heatmap */}
      <Heatmap data={heatmapData} />

      {/* Two Column Grid */}
      <div className="dashboard-grid">
        {/* Current Phase */}
        <div className="card animate-fade-in-up">
          <h3 className="card-title">Current Phase</h3>
          {dashboard.currentPhase ? (
            <div className="current-phase-card">
              <div className="current-phase-ring">
                <ProgressRing
                  progress={dashboard.currentPhase.progressPercentage}
                  size={90}
                  strokeWidth={7}
                />
              </div>
              <div className="current-phase-info">
                <div className="current-phase-label">
                  Phase {dashboard.currentPhase.phaseNumber}
                </div>
                <div className="current-phase-title">
                  {dashboard.currentPhase.icon} {dashboard.currentPhase.title}
                </div>
                <div className="current-phase-desc">
                  {dashboard.currentPhase.description}
                </div>
                <div className="current-phase-progress-text">
                  <strong>{dashboard.currentPhase.completedTopics}</strong> of{' '}
                  {dashboard.currentPhase.totalTopics} topics completed
                </div>
              </div>
            </div>
          ) : (
            <div className="empty-state" style={{ padding: 'var(--space-8)' }}>
              <span className="empty-state-icon">🎉</span>
              <h3 className="empty-state-title">All phases completed!</h3>
              <p className="empty-state-text">You've completed the entire curriculum. Amazing!</p>
            </div>
          )}
        </div>

        {/* Recent Activity */}
        <div className="card animate-fade-in-up">
          <h3 className="card-title">Recent Activity</h3>
          {dashboard.recentActivities?.length > 0 ? (
            <div className="recent-activity-list">
              {dashboard.recentActivities.slice(0, 8).map(activity => {
                const { icon, cls } = getActivityIcon(activity.activityType)
                return (
                  <div key={activity.id} className="activity-item">
                    <div className={`activity-icon ${cls}`}>
                      {icon}
                    </div>
                    <div className="activity-content">
                      <div className="activity-desc">{activity.description}</div>
                      <div className="activity-meta">
                        <span>{formatTimeAgo(activity.createdAt)}</span>
                        {activity.xpEarned > 0 && (
                          <span className="activity-xp">+{activity.xpEarned} XP</span>
                        )}
                        {activity.minutesSpent > 0 && (
                          <span>{activity.minutesSpent} min</span>
                        )}
                      </div>
                    </div>
                  </div>
                )
              })}
            </div>
          ) : (
            <div className="empty-state" style={{ padding: 'var(--space-8)' }}>
              <span className="empty-state-icon">🌟</span>
              <h3 className="empty-state-title">No activity yet</h3>
              <p className="empty-state-text">Start completing topics to see your activity here!</p>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
