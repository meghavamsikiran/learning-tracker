package com.megha.learningtracker.controller;

import com.megha.learningtracker.dto.*;
import com.megha.learningtracker.service.ActivityService;
import com.megha.learningtracker.service.CurriculumService;
import com.megha.learningtracker.service.StatsService;
import com.megha.learningtracker.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final StatsService statsService;
    private final CurriculumService curriculumService;
    private final ActivityService activityService;
    private final StreakService streakService;

    /**
     * GET /api/dashboard
     * Returns the full dashboard data: stats, current phase, recent activities.
     */
    @GetMapping
    public ResponseEntity<DashboardDTO> getDashboard() {
        StatsDTO stats = statsService.getStats();
        PhaseDTO currentPhase = curriculumService.getCurrentPhase();
        List<ActivityDTO> recentActivities = activityService.getRecentActivities(10);

        DashboardDTO dashboard = DashboardDTO.builder()
                .currentStreak(stats.getCurrentStreak())
                .longestStreak(stats.getLongestStreak())
                .totalTopicsCompleted(stats.getTotalTopicsCompleted())
                .totalTopics(stats.getTotalTopics())
                .totalXp(stats.getTotalXp())
                .level(stats.getLevel())
                .totalStudyMinutes(stats.getTotalStudyMinutes())
                .completionPercentage(stats.getCompletionPercentage())
                .currentPhase(currentPhase)
                .recentActivities(recentActivities)
                .build();

        return ResponseEntity.ok(dashboard);
    }

    /**
     * GET /api/dashboard/heatmap
     * Returns 365 days of activity data for the contribution heatmap.
     */
    @GetMapping("/heatmap")
    public ResponseEntity<List<HeatmapEntryDTO>> getHeatmap() {
        return ResponseEntity.ok(activityService.getHeatmapData());
    }
}
