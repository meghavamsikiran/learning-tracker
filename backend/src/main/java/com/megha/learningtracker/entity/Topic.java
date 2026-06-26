package com.megha.learningtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "topics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "estimated_minutes")
    @Builder.Default
    private int estimatedMinutes = 60;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private TopicStatus status = TopicStatus.NOT_STARTED;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "xp_reward")
    @Builder.Default
    private int xpReward = 10;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "sort_order")
    @Builder.Default
    private int sortOrder = 0;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
