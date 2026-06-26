import { NavLink, useLocation } from 'react-router-dom'
import { useState } from 'react'

export default function Sidebar({ stats }) {
  const [isOpen, setIsOpen] = useState(false)
  const location = useLocation()

  const navItems = [
    { path: '/', icon: '📊', label: 'Dashboard' },
    { path: '/curriculum', icon: '📚', label: 'Curriculum' },
    { path: '/activity', icon: '📝', label: 'Activity Log' },
    { path: '/stats', icon: '📈', label: 'Statistics' },
  ]

  return (
    <>
      <button className="sidebar-toggle" onClick={() => setIsOpen(!isOpen)}>
        {isOpen ? '✕' : '☰'}
      </button>

      {isOpen && <div className="sidebar-overlay visible" onClick={() => setIsOpen(false)} />}

      <nav className={`sidebar ${isOpen ? 'open' : ''}`}>
        {/* Brand */}
        <div className="sidebar-header">
          <div className="sidebar-brand">
            <span className="sidebar-brand-icon">🚀</span>
            <div>
              <div className="sidebar-brand-text">Learning Tracker</div>
              <div className="sidebar-brand-sub">Java Backend Mentorship</div>
            </div>
          </div>
        </div>

        {/* Navigation */}
        <div className="sidebar-nav">
          <div className="sidebar-section-title">Navigation</div>
          {navItems.map(item => (
            <NavLink
              key={item.path}
              to={item.path}
              end={item.path === '/'}
              className={({ isActive }) => `sidebar-nav-item ${isActive ? 'active' : ''}`}
              onClick={() => setIsOpen(false)}
            >
              <span className="sidebar-nav-icon">{item.icon}</span>
              {item.label}
            </NavLink>
          ))}
        </div>

        {/* Quick Stats */}
        {stats && (
          <div className="sidebar-stats">
            <div className="sidebar-section-title" style={{ padding: '0 0 var(--space-3)' }}>Quick Stats</div>
            <div className="sidebar-stat">
              <span className="sidebar-stat-label">🔥 Streak</span>
              <span className="sidebar-stat-value">{stats.currentStreak} days</span>
            </div>
            <div className="sidebar-stat">
              <span className="sidebar-stat-label">✅ Completed</span>
              <span className="sidebar-stat-value">{stats.totalTopicsCompleted}/{stats.totalTopics}</span>
            </div>
            <div className="sidebar-stat">
              <span className="sidebar-stat-label">⚡ Level</span>
              <span className="sidebar-stat-value">{stats.level}</span>
            </div>
            <div className="sidebar-stat">
              <span className="sidebar-stat-label">🏆 XP</span>
              <span className="sidebar-stat-value">{stats.totalXp?.toLocaleString()}</span>
            </div>
          </div>
        )}
      </nav>
    </>
  )
}
