import { useState, useRef, useEffect, useMemo } from 'react'

export default function Heatmap({ data }) {
  const [tooltip, setTooltip] = useState(null)
  const containerRef = useRef(null)

  // Build weeks grid from data (365 days)
  const { weeks, months, totalMinutes, activeDays } = useMemo(() => {
    if (!data || data.length === 0) return { weeks: [], months: [], totalMinutes: 0, activeDays: 0 }

    let totalMin = 0
    let active = 0

    // Group data into weeks (columns)
    const weeksArr = []
    let currentWeek = []

    // Find the start day (must start on Sunday)
    const firstDate = new Date(data[0].date)
    const startDay = firstDate.getDay()

    // Add empty cells for the first week
    for (let i = 0; i < startDay; i++) {
      currentWeek.push(null)
    }

    data.forEach(entry => {
      if (entry.minutes > 0 || entry.topicsCompleted > 0) {
        active++
        totalMin += entry.minutes
      }

      currentWeek.push(entry)
      if (currentWeek.length === 7) {
        weeksArr.push(currentWeek)
        currentWeek = []
      }
    })

    if (currentWeek.length > 0) {
      weeksArr.push(currentWeek)
    }

    // Calculate month labels with positions
    const monthLabels = []
    let prevMonth = -1
    data.forEach((entry, i) => {
      const date = new Date(entry.date)
      const month = date.getMonth()
      if (month !== prevMonth) {
        const weekIndex = Math.floor((i + startDay) / 7)
        monthLabels.push({
          label: date.toLocaleString('default', { month: 'short' }),
          position: weekIndex,
        })
        prevMonth = month
      }
    })

    return { weeks: weeksArr, months: monthLabels, totalMinutes: totalMin, activeDays: active }
  }, [data])

  function getLevel(minutes) {
    if (minutes <= 0) return 0
    if (minutes <= 30) return 1
    if (minutes <= 60) return 2
    if (minutes <= 90) return 3
    return 4
  }

  function handleCellHover(e, entry) {
    if (!entry) return
    const rect = e.target.getBoundingClientRect()
    setTooltip({
      x: rect.left + rect.width / 2,
      y: rect.top - 8,
      date: new Date(entry.date).toLocaleDateString('en-US', {
        weekday: 'short', month: 'short', day: 'numeric', year: 'numeric'
      }),
      minutes: entry.minutes,
      topics: entry.topicsCompleted,
    })
  }

  const dayLabels = ['', 'Mon', '', 'Wed', '', 'Fri', '']

  return (
    <div className="heatmap-section" ref={containerRef}>
      <div className="heatmap-header">
        <h3 className="heatmap-title">
          📊 Activity Overview
        </h3>
        <span className="heatmap-total">
          <strong>{activeDays}</strong> active days · <strong>{Math.round(totalMinutes / 60)}</strong> total hours
        </span>
      </div>

      <div className="heatmap-wrapper">
        <div className="heatmap-container">
          {/* Month labels */}
          <div className="heatmap-months">
            {months.map((m, i) => (
              <span
                key={i}
                className="heatmap-month-label"
                style={{
                  position: 'absolute',
                  left: `${32 + m.position * 16}px`,
                }}
              >
                {m.label}
              </span>
            ))}
          </div>

          <div className="heatmap-grid" style={{ marginTop: '20px' }}>
            {/* Day labels */}
            <div className="heatmap-day-labels">
              {dayLabels.map((label, i) => (
                <span key={i} className="heatmap-day-label">{label}</span>
              ))}
            </div>

            {/* Weeks grid */}
            <div className="heatmap-weeks">
              {weeks.map((week, wi) => (
                <div key={wi} className="heatmap-week">
                  {week.map((day, di) => (
                    <div
                      key={`${wi}-${di}`}
                      className="heatmap-cell"
                      data-level={day ? getLevel(day.minutes) : 0}
                      onMouseEnter={(e) => day && handleCellHover(e, day)}
                      onMouseLeave={() => setTooltip(null)}
                    />
                  ))}
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Legend */}
        <div className="heatmap-legend">
          <span className="heatmap-legend-label">Less</span>
          <div className="heatmap-legend-cells">
            {[0, 1, 2, 3, 4].map(level => (
              <div
                key={level}
                className="heatmap-legend-cell"
                style={{ background: `var(--heatmap-${level})` }}
              />
            ))}
          </div>
          <span className="heatmap-legend-label">More</span>
        </div>
      </div>

      {/* Tooltip */}
      {tooltip && (
        <div
          className="heatmap-tooltip"
          style={{
            left: tooltip.x,
            top: tooltip.y,
            transform: 'translate(-50%, -100%)',
          }}
        >
          <div className="heatmap-tooltip-date">{tooltip.date}</div>
          <div className="heatmap-tooltip-value">
            {tooltip.minutes > 0 ? `${tooltip.minutes} min studied` : 'No activity'}
            {tooltip.topics > 0 && ` · ${tooltip.topics} topics`}
          </div>
        </div>
      )}
    </div>
  )
}
