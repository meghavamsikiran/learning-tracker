import { useState, useEffect } from 'react'
import { fetchStats, fetchCurriculum } from '../api/client'
import ProgressRing from '../components/common/ProgressRing'

export default function Statistics() {
  const [stats, setStats] = useState(null)
  const [phases, setPhases] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    Promise.all([fetchStats(), fetchCurriculum()])
      .then(([s, p]) => {
        setStats(s)
        setPhases(p)
      })
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return <div className="loading"><div className="loading-spinner" /></div>
  }

  if (!stats) {
    return (
      <div className="empty-state">
        <span className="empty-state-icon">📈</span>
        <h3 className="empty-state-title">No data yet</h3>
        <p className="empty-state-text">Complete some topics to see your statistics.</p>
      </div>
    )
  }

  return (
    <div className="animate-fade-in">
      <div className="page-header">
        <h1 className="page-title">Statistics</h1>
        <p className="page-subtitle">Your complete learning analytics</p>
      </div>

      {/* Stats Grid */}
      <div className="stats-grid stagger-children">
        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">⚡</span>
          <span className="stat-card-value">{stats.totalXp?.toLocaleString()}</span>
          <span className="stat-card-label">Total XP (Level {stats.level})</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">🎯</span>
          <span className="stat-card-value">{stats.xpToNextLevel}</span>
          <span className="stat-card-label">XP to Level {stats.level + 1}</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">⏱️</span>
          <span className="stat-card-value">{stats.totalStudyHours}h</span>
          <span className="stat-card-label">Total Study Time</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">🔥</span>
          <span className="stat-card-value">{stats.currentStreak}</span>
          <span className="stat-card-label">Current Streak</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">🏆</span>
          <span className="stat-card-value">{stats.longestStreak}</span>
          <span className="stat-card-label">Longest Streak</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">📅</span>
          <span className="stat-card-value">{stats.activeDays}</span>
          <span className="stat-card-label">Active Days</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">📊</span>
          <span className="stat-card-value">{Math.round(stats.avgMinutesPerDay)}m</span>
          <span className="stat-card-label">Avg. Min / Active Day</span>
        </div>

        <div className="stat-card animate-fade-in-up">
          <span className="stat-card-icon">🗓️</span>
          <span className="stat-card-value">
            {stats.predictedDaysRemaining > 0 ? `${stats.predictedDaysRemaining}d` : '—'}
          </span>
          <span className="stat-card-label">Est. Days Remaining</span>
        </div>
      </div>

      {/* Charts Row */}
      <div className="stats-charts">
        {/* Completion Donut */}
        <div className="card animate-fade-in-up">
          <h3 className="card-title">Completion Progress</h3>
          <div className="donut-chart-container">
            <ProgressRing
              progress={stats.completionPercentage}
              size={180}
              strokeWidth={12}
            />
            <div style={{ textAlign: 'center' }}>
              <div style={{ fontSize: 'var(--font-sm)', color: 'var(--text-muted)', marginTop: 'var(--space-2)' }}>
                <strong style={{ color: 'var(--accent-green)' }}>{stats.totalTopicsCompleted}</strong> completed ·{' '}
                <strong style={{ color: 'var(--text-secondary)' }}>{stats.totalTopics - stats.totalTopicsCompleted}</strong> remaining
              </div>
              <div style={{ fontSize: 'var(--font-sm)', color: 'var(--text-muted)', marginTop: 'var(--space-2)' }}>
                <strong>{stats.phasesCompleted}</strong> of <strong>{stats.totalPhases}</strong> phases completed
              </div>
              {stats.estimatedCompletionDate && (
                <div style={{ fontSize: 'var(--font-sm)', color: 'var(--accent-purple)', marginTop: 'var(--space-3)' }}>
                  📅 Est. completion: {new Date(stats.estimatedCompletionDate).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}
                </div>
              )}
            </div>
          </div>
        </div>

        {/* Phase Timeline */}
        <div className="card animate-fade-in-up">
          <h3 className="card-title">Phase Progress</h3>
          <div className="phase-timeline">
            {phases.map(phase => (
              <div key={phase.id} className="phase-timeline-item">
                <div className={`phase-timeline-indicator ${phase.status === 'COMPLETED' ? 'completed' : phase.status === 'IN_PROGRESS' ? 'in-progress' : 'not-started'}`} />
                <div className="phase-timeline-info">
                  <div className="phase-timeline-title">
                    {phase.icon} Phase {phase.phaseNumber}: {phase.title}
                  </div>
                  <div className="phase-timeline-progress">
                    {phase.completedTopics}/{phase.totalTopics} topics · {Math.round(phase.progressPercentage)}%
                  </div>
                </div>
                <div style={{ minWidth: '80px' }}>
                  <div className="progress-bar progress-bar-sm">
                    <div className="progress-bar-fill" style={{ width: `${phase.progressPercentage}%` }} />
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* XP Level Progress */}
      <div className="card animate-fade-in-up" style={{ marginTop: 'var(--space-6)' }}>
        <h3 className="card-title">⚡ XP Progress — Level {stats.level}</h3>
        <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 'var(--space-2)' }}>
          <span style={{ fontSize: 'var(--font-sm)', color: 'var(--text-muted)' }}>
            {500 - stats.xpToNextLevel} / 500 XP
          </span>
          <span style={{ fontSize: 'var(--font-sm)', color: 'var(--accent-amber)', fontWeight: 600 }}>
            {stats.xpToNextLevel} XP to Level {stats.level + 1}
          </span>
        </div>
        <div className="progress-bar">
          <div
            className="progress-bar-fill"
            style={{
              width: `${((500 - stats.xpToNextLevel) / 500) * 100}%`,
              background: 'linear-gradient(90deg, var(--accent-amber), var(--accent-purple))',
            }}
          />
        </div>
      </div>
    </div>
  )
}
