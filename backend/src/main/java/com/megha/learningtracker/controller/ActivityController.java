package com.megha.learningtracker.controller;

import com.megha.learningtracker.dto.ActivityDTO;
import com.megha.learningtracker.dto.StudyLogRequest;
import com.megha.learningtracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    /**
     * GET /api/activities?limit=50
     * Returns recent activities (default limit: 50).
     */
    @GetMapping
    public ResponseEntity<List<ActivityDTO>> getActivities(
            @RequestParam(defaultValue = "50") int limit) {
        return ResponseEntity.ok(activityService.getRecentActivities(limit));
    }

    /**
     * GET /api/activities/grouped?limit=100
     * Returns activities grouped by date.
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<LocalDate, List<ActivityDTO>>> getGroupedActivities(
            @RequestParam(defaultValue = "100") int limit) {
        return ResponseEntity.ok(activityService.getActivitiesGroupedByDate(limit));
    }

    /**
     * GET /api/activities/range?start=2026-01-01&end=2026-06-25
     * Returns activities within a date range.
     */
    @GetMapping("/range")
    public ResponseEntity<List<ActivityDTO>> getActivitiesByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(activityService.getActivitiesByDateRange(start, end));
    }

    /**
     * POST /api/activities/log
     * Manually log study time.
     */
    @PostMapping("/log")
    public ResponseEntity<ActivityDTO> logStudyTime(@RequestBody StudyLogRequest request) {
        return ResponseEntity.ok(activityService.logStudyTime(request));
    }
}
