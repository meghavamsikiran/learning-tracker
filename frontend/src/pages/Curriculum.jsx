import { useState, useEffect, useCallback } from 'react'
import { fetchCurriculum, completeTopic, updateTopic } from '../api/client'
import Modal from '../components/common/Modal'
import Confetti from '../components/common/Confetti'
import { useToast } from '../components/common/ToastProvider'

export default function Curriculum() {
  const [phases, setPhases] = useState([])
  const [loading, setLoading] = useState(true)
  const [expandedPhase, setExpandedPhase] = useState(null)
  const [selectedTopic, setSelectedTopic] = useState(null)
  const [filter, setFilter] = useState('ALL')
  const [confettiTrigger, setConfettiTrigger] = useState(0)
  const [notes, setNotes] = useState('')
  const [minutesInput, setMinutesInput] = useState('')
  const { addToast } = useToast()

  const loadCurriculum = useCallback(() => {
    fetchCurriculum()
      .then(setPhases)
      .catch(console.error)
      .finally(() => setLoading(false))
  }, [])

  useEffect(() => { loadCurriculum() }, [loadCurriculum])

  function togglePhase(id) {
    setExpandedPhase(prev => prev === id ? null : id)
  }

  function openTopic(topic) {
    setSelectedTopic(topic)
    setNotes(topic.notes || '')
    setMinutesInput(topic.estimatedMinutes?.toString() || '60')
  }

  async function handleComplete() {
    if (!selectedTopic) return
    try {
      const minutes = parseInt(minutesInput) || selectedTopic.estimatedMinutes
      await completeTopic(selectedTopic.id, minutes)

      // Save notes if any
      if (notes !== (selectedTopic.notes || '')) {
        await updateTopic(selectedTopic.id, { notes })
      }

      setConfettiTrigger(prev => prev + 1)
      addToast(`🎉 Completed: ${selectedTopic.title} (+${selectedTopic.xpReward} XP)`, 'success')
      setSelectedTopic(null)
      loadCurriculum()
    } catch (err) {
      addToast('Failed to complete topic', 'error')
    }
  }

  async function handleSaveNotes() {
    if (!selectedTopic) return
    try {
      await updateTopic(selectedTopic.id, { notes })
      addToast('Notes saved!', 'success')
      loadCurriculum()
    } catch (err) {
      addToast('Failed to save notes', 'error')
    }
  }

  async function handleStartTopic() {
    if (!selectedTopic) return
    try {
      await updateTopic(selectedTopic.id, { status: 'IN_PROGRESS' })
      addToast(`Started: ${selectedTopic.title}`, 'info')
      setSelectedTopic(null)
      loadCurriculum()
    } catch (err) {
      addToast('Failed to start topic', 'error')
    }
  }

  // Filter phases
  const filteredPhases = phases.filter(phase => {
    if (filter === 'ALL') return true
    return phase.status === filter
  })

  // Summary
  const totalTopics = phases.reduce((sum, p) => sum + p.totalTopics, 0)
  const completedTopics = phases.reduce((sum, p) => sum + p.completedTopics, 0)
  const completedPhases = phases.filter(p => p.status === 'COMPLETED').length

  if (loading) {
    return <div className="loading"><div className="loading-spinner" /></div>
  }

  return (
    <div className="animate-fade-in">
      <Confetti trigger={confettiTrigger} />

      {/* Header */}
      <div className="curriculum-header">
        <div>
          <h1 className="page-title">Curriculum</h1>
          <div className="curriculum-overview">
            <span><strong>{phases.length}</strong> Phases</span>
            <span><strong>{completedTopics}</strong>/<strong>{totalTopics}</strong> Topics</span>
            <span><strong>{completedPhases}</strong> Completed</span>
          </div>
        </div>

        <div className="filter-tabs">
          {['ALL', 'NOT_STARTED', 'IN_PROGRESS', 'COMPLETED'].map(f => (
            <button
              key={f}
              className={`filter-tab ${filter === f ? 'active' : ''}`}
              onClick={() => setFilter(f)}
            >
              {f === 'ALL' ? 'All' : f === 'NOT_STARTED' ? 'Not Started' : f === 'IN_PROGRESS' ? 'In Progress' : 'Completed'}
            </button>
          ))}
        </div>
      </div>

      {/* Phase List */}
      <div className="phase-list">
        {filteredPhases.map(phase => (
          <div key={phase.id} className={`phase-accordion ${expandedPhase === phase.id ? 'expanded' : ''}`}>
            {/* Phase Header */}
            <div className="phase-header" onClick={() => togglePhase(phase.id)}>
              <div className={`phase-number ${phase.status === 'COMPLETED' ? 'completed' : phase.status === 'IN_PROGRESS' ? 'in-progress' : ''}`}>
                {phase.status === 'COMPLETED' ? '✓' : phase.phaseNumber}
              </div>

              <div className="phase-info">
                <div className="phase-title">
                  {phase.icon} {phase.title}
                </div>
                <div className="phase-meta">
                  {phase.completedTopics}/{phase.totalTopics} topics ·{' '}
                  {phase.status === 'COMPLETED' ? (
                    <span style={{ color: 'var(--accent-green)' }}>Complete</span>
                  ) : phase.status === 'IN_PROGRESS' ? (
                    <span style={{ color: 'var(--accent-amber)' }}>In Progress</span>
                  ) : (
                    <span style={{ color: 'var(--text-dim)' }}>Not Started</span>
                  )}
                </div>
              </div>

              <div className="phase-progress-section">
                <div className="progress-bar progress-bar-sm phase-progress-bar">
                  <div
                    className="progress-bar-fill"
                    style={{ width: `${phase.progressPercentage}%` }}
                  />
                </div>
                <span className="phase-progress-text">
                  {Math.round(phase.progressPercentage)}%
                </span>
              </div>

              <span className="phase-chevron">▼</span>
            </div>

            {/* Topics */}
            {expandedPhase === phase.id && (
              <div className="phase-topics animate-fade-in">
                {phase.topics?.map(topic => (
                  <div
                    key={topic.id}
                    className="topic-item"
                    onClick={() => openTopic(topic)}
                  >
                    <div className={`topic-status-icon ${topic.status === 'COMPLETED' ? 'completed' : topic.status === 'IN_PROGRESS' ? 'in-progress' : ''}`}>
                      {topic.status === 'COMPLETED' ? '✓' :
                        topic.status === 'IN_PROGRESS' ? '▶' : ''}
                    </div>
                    <div className="topic-info">
                      <div className={`topic-title ${topic.status === 'COMPLETED' ? 'completed' : ''}`}>
                        {topic.title}
                      </div>
                      <div className="topic-desc">{topic.description}</div>
                    </div>
                    <span className="topic-time">{topic.estimatedMinutes}m</span>
                    <span className="topic-xp">+{topic.xpReward} XP</span>
                  </div>
                ))}
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Topic Detail Modal */}
      <Modal
        isOpen={!!selectedTopic}
        onClose={() => setSelectedTopic(null)}
        title={selectedTopic?.title || ''}
        footer={
          selectedTopic && (
            <>
              {selectedTopic.status !== 'COMPLETED' && selectedTopic.status !== 'IN_PROGRESS' && (
                <button className="btn btn-ghost" onClick={handleStartTopic}>
                  ▶ Start Topic
                </button>
              )}
              <button className="btn btn-ghost" onClick={handleSaveNotes}>
                💾 Save Notes
              </button>
              {selectedTopic.status !== 'COMPLETED' && (
                <button className="btn btn-success" onClick={handleComplete}>
                  ✅ Mark Complete
                </button>
              )}
            </>
          )
        }
      >
        {selectedTopic && (
          <>
            <div className="topic-detail-phase">
              Phase {selectedTopic.phaseNumber} · {selectedTopic.phaseTitle}
            </div>

            <div className="topic-detail-stats">
              <div className="topic-detail-stat">
                <div className="topic-detail-stat-value">{selectedTopic.estimatedMinutes}m</div>
                <div className="topic-detail-stat-label">Est. Time</div>
              </div>
              <div className="topic-detail-stat">
                <div className="topic-detail-stat-value">+{selectedTopic.xpReward}</div>
                <div className="topic-detail-stat-label">XP Reward</div>
              </div>
              <div className="topic-detail-stat">
                <div className="topic-detail-stat-value">
                  <span className={`badge ${selectedTopic.status === 'COMPLETED' ? 'badge-green' : selectedTopic.status === 'IN_PROGRESS' ? 'badge-amber' : 'badge-muted'}`}>
                    {selectedTopic.status === 'COMPLETED' ? 'Done' : selectedTopic.status === 'IN_PROGRESS' ? 'Active' : 'Todo'}
                  </span>
                </div>
                <div className="topic-detail-stat-label">Status</div>
              </div>
            </div>

            <div className="topic-detail-description">
              {selectedTopic.description}
            </div>

            {selectedTopic.status !== 'COMPLETED' && (
              <div style={{ marginBottom: 'var(--space-4)' }}>
                <label style={{ fontSize: 'var(--font-sm)', fontWeight: 600, color: 'var(--text-secondary)', display: 'block', marginBottom: 'var(--space-2)' }}>
                  Minutes Spent
                </label>
                <input
                  type="number"
                  value={minutesInput}
                  onChange={e => setMinutesInput(e.target.value)}
                  min="1"
                  max="480"
                  style={{ width: '120px' }}
                />
              </div>
            )}

            <div className="topic-detail-notes">
              <label>📝 Personal Notes</label>
              <textarea
                value={notes}
                onChange={e => setNotes(e.target.value)}
                placeholder="Add your notes, key learnings, or interview answers here..."
              />
            </div>

            {selectedTopic.completedAt && (
              <div style={{ fontSize: 'var(--font-xs)', color: 'var(--text-dim)' }}>
                Completed on {new Date(selectedTopic.completedAt).toLocaleDateString()}
              </div>
            )}
          </>
        )}
      </Modal>
    </div>
  )
}
