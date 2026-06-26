import { useState, useEffect } from 'react'
import { fetchActivities, logStudyTime } from '../api/client'
import { useToast } from '../components/common/ToastProvider'

export default function ActivityLog() {
  const [activities, setActivities] = useState([])
  const [loading, setLoading] = useState(true)
  const [minutes, setMinutes] = useState('')
  const [logDate, setLogDate] = useState(new Date().toISOString().split('T')[0])
  const { addToast } = useToast()

  useEffect(() => {
    loadActivities()
  }, [])

  function loadActivities() {
    fetchActivities(200)
      .then(setActivities)
      .catch(console.error)
      .finally(() => setLoading(false))
  }

  async function handleLogTime(e) {
    e.preventDefault()
    if (!minutes || parseInt(minutes) <= 0) {
      addToast('Please enter valid minutes', 'error')
      return
    }
    try {
      await logStudyTime({ minutes: parseInt(minutes), date: logDate })
      addToast(`Logged ${minutes} minutes of study time!`, 'success')
      setMinutes('')
      loadActivities()
    } catch (err) {
      addToast('Failed to log study time', 'error')
    }
  }

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

  // Group activities by date
  const grouped = activities.reduce((acc, activity) => {
    const date = activity.activityDate
    if (!acc[date]) acc[date] = []
    acc[date].push(activity)
    return acc
  }, {})

  function formatDate(dateStr) {
    const date = new Date(dateStr + 'T00:00:00')
    const today = new Date()
    today.setHours(0, 0, 0, 0)
    const yesterday = new Date(today)
    yesterday.setDate(yesterday.getDate() - 1)

    if (date.getTime() === today.getTime()) return 'Today'
    if (date.getTime() === yesterday.getTime()) return 'Yesterday'
    return date.toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric', year: 'numeric' })
  }

  if (loading) {
    return <div className="loading"><div className="loading-spinner" /></div>
  }

  return (
    <div className="animate-fade-in">
      {/* Header */}
      <div className="activity-log-header">
        <div>
          <h1 className="page-title">Activity Log</h1>
          <p className="page-subtitle">{activities.length} total activities recorded</p>
        </div>

        {/* Log Study Time Form */}
        <form className="log-form" onSubmit={handleLogTime}>
          <div className="log-form-group">
            <label>Minutes</label>
            <input
              type="number"
              value={minutes}
              onChange={e => setMinutes(e.target.value)}
              placeholder="60"
              min="1"
              max="480"
            />
          </div>
          <div className="log-form-group">
            <label>Date</label>
            <input
              type="date"
              value={logDate}
              onChange={e => setLogDate(e.target.value)}
              style={{ width: '150px' }}
            />
          </div>
          <button type="submit" className="btn btn-primary btn-sm">
            ⏱️ Log Time
          </button>
        </form>
      </div>

      {/* Activity Feed */}
      {Object.keys(grouped).length > 0 ? (
        Object.entries(grouped)
          .sort(([a], [b]) => b.localeCompare(a))
          .map(([date, items]) => (
            <div key={date} className="activity-date-group">
              <div className="activity-date-header">{formatDate(date)}</div>
              <div className="recent-activity-list">
                {items.map(activity => {
                  const { icon, cls } = getActivityIcon(activity.activityType)
                  return (
                    <div key={activity.id} className="activity-item">
                      <div className={`activity-icon ${cls}`}>{icon}</div>
                      <div className="activity-content">
                        <div className="activity-desc">{activity.description}</div>
                        <div className="activity-meta">
                          {activity.phaseTitle && <span>Phase {activity.phaseNumber}: {activity.phaseTitle}</span>}
                          {activity.xpEarned > 0 && <span className="activity-xp">+{activity.xpEarned} XP</span>}
                          {activity.minutesSpent > 0 && <span>{activity.minutesSpent} min</span>}
                        </div>
                      </div>
                    </div>
                  )
                })}
              </div>
            </div>
          ))
      ) : (
        <div className="empty-state">
          <span className="empty-state-icon">📝</span>
          <h3 className="empty-state-title">No activities yet</h3>
          <p className="empty-state-text">Complete topics or log study time to see your activity here.</p>
        </div>
      )}
    </div>
  )
}
