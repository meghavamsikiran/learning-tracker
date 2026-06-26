package com.megha.learningtracker.controller;

import com.megha.learningtracker.dto.StatsDTO;
import com.megha.learningtracker.service.StatsService;
import com.megha.learningtracker.service.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final StreakService streakService;

    /**
     * GET /api/stats
     * Returns all statistics.
     */
    @GetMapping
    public ResponseEntity<StatsDTO> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }

    /**
     * GET /api/stats/streaks
     * Returns current and longest streak.
     */
    @GetMapping("/streaks")
    public ResponseEntity<Map<String, Integer>> getStreaks() {
        return ResponseEntity.ok(Map.of(
                "currentStreak", streakService.getCurrentStreak(),
                "longestStreak", streakService.getLongestStreak()
        ));
    }
}
