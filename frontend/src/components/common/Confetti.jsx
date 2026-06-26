import { useEffect, useRef } from 'react'

export default function Confetti({ trigger }) {
  const canvasRef = useRef(null)

  useEffect(() => {
    if (!trigger) return
    const canvas = canvasRef.current
    if (!canvas) return

    const ctx = canvas.getContext('2d')
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight

    const particles = []
    const colors = ['#22c55e', '#8b5cf6', '#f59e0b', '#3b82f6', '#ef4444', '#06b6d4', '#ec4899']

    for (let i = 0; i < 120; i++) {
      particles.push({
        x: canvas.width / 2 + (Math.random() - 0.5) * 200,
        y: canvas.height / 2,
        vx: (Math.random() - 0.5) * 15,
        vy: Math.random() * -18 - 5,
        color: colors[Math.floor(Math.random() * colors.length)],
        size: Math.random() * 8 + 3,
        rotation: Math.random() * 360,
        rotationSpeed: (Math.random() - 0.5) * 10,
        gravity: 0.3,
        opacity: 1,
        decay: 0.015 + Math.random() * 0.01,
      })
    }

    let animFrame
    function animate() {
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      let alive = false

      particles.forEach(p => {
        if (p.opacity <= 0) return
        alive = true

        p.x += p.vx
        p.vy += p.gravity
        p.y += p.vy
        p.rotation += p.rotationSpeed
        p.opacity -= p.decay
        p.vx *= 0.99

        ctx.save()
        ctx.translate(p.x, p.y)
        ctx.rotate((p.rotation * Math.PI) / 180)
        ctx.globalAlpha = Math.max(0, p.opacity)
        ctx.fillStyle = p.color
        ctx.fillRect(-p.size / 2, -p.size / 2, p.size, p.size * 0.6)
        ctx.restore()
      })

      if (alive) {
        animFrame = requestAnimationFrame(animate)
      }
    }

    animate()
    return () => cancelAnimationFrame(animFrame)
  }, [trigger])

  if (!trigger) return null

  return (
    <canvas
      ref={canvasRef}
      style={{
        position: 'fixed',
        inset: 0,
        pointerEvents: 'none',
        zIndex: 9999,
      }}
    />
  )
}
