package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardDTO {
    private int currentStreak;
    private int longestStreak;
    private int totalTopicsCompleted;
    private int totalTopics;
    private int totalXp;
    private int level;
    private int totalStudyMinutes;
    private double completionPercentage;
    private PhaseDTO currentPhase;
    private List<ActivityDTO> recentActivities;
}
