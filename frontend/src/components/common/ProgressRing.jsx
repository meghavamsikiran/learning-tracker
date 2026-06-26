export default function ProgressRing({ progress, size = 80, strokeWidth = 6, color = 'var(--accent-green)' }) {
  const radius = (size - strokeWidth) / 2
  const circumference = radius * 2 * Math.PI
  const offset = circumference - (progress / 100) * circumference

  return (
    <div className="donut-chart" style={{ width: size, height: size }}>
      <svg width={size} height={size}>
        {/* Background circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke="var(--bg-tertiary)"
          strokeWidth={strokeWidth}
        />
        {/* Progress circle */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={radius}
          fill="none"
          stroke={color}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={offset}
          style={{
            transition: 'stroke-dashoffset 0.8s cubic-bezier(0.34, 1.56, 0.64, 1)',
          }}
        />
      </svg>
      <div className="donut-chart-center">
        <span className="donut-chart-value" style={{ fontSize: size > 100 ? 'var(--font-2xl)' : 'var(--font-md)' }}>
          {Math.round(progress)}%
        </span>
      </div>
    </div>
  )
}
