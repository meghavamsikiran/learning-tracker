package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatsDTO {
    private int totalXp;
    private int level;
    private int xpToNextLevel;
    private int totalStudyMinutes;
    private double totalStudyHours;
    private int totalTopicsCompleted;
    private int totalTopics;
    private double completionPercentage;
    private int currentStreak;
    private int longestStreak;
    private int phasesCompleted;
    private int totalPhases;
    private double avgMinutesPerDay;
    private int activeDays;
    private int predictedDaysRemaining;
    private LocalDate estimatedCompletionDate;
}
