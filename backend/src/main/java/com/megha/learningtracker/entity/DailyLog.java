package com.megha.learningtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_date", nullable = false, unique = true)
    private LocalDate logDate;

    @Column(name = "total_minutes")
    @Builder.Default
    private int totalMinutes = 0;

    @Column(name = "topics_completed")
    @Builder.Default
    private int topicsCompleted = 0;

    @Column(name = "xp_earned")
    @Builder.Default
    private int xpEarned = 0;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
