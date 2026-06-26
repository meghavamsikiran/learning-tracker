package com.megha.learningtracker.service;

import com.megha.learningtracker.dto.ActivityDTO;
import com.megha.learningtracker.dto.HeatmapEntryDTO;
import com.megha.learningtracker.dto.StudyLogRequest;
import com.megha.learningtracker.entity.*;
import com.megha.learningtracker.repository.ActivityRepository;
import com.megha.learningtracker.repository.DailyLogRepository;
import com.megha.learningtracker.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final DailyLogRepository dailyLogRepository;
    private final TopicRepository topicRepository;

    /**
     * Get recent activities (paginated).
     */
    @Transactional(readOnly = true)
    public List<ActivityDTO> getRecentActivities(int limit) {
        return activityRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(0, limit))
                .stream()
                .map(this::toActivityDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get activities within a date range.
     */
    @Transactional(readOnly = true)
    public List<ActivityDTO> getActivitiesByDateRange(LocalDate start, LocalDate end) {
        return activityRepository.findByDateRange(start, end).stream()
                .map(this::toActivityDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all activities grouped by date.
     */
    @Transactional(readOnly = true)
    public Map<LocalDate, List<ActivityDTO>> getActivitiesGroupedByDate(int limit) {
        List<ActivityDTO> activities = getRecentActivities(limit);
        return activities.stream()
                .collect(Collectors.groupingBy(
                        ActivityDTO::getActivityDate,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * Manually log study time.
     */
    @Transactional
    public ActivityDTO logStudyTime(StudyLogRequest request) {
        LocalDate date = request.getDate() != null ? request.getDate() : LocalDate.now();

        Topic topic = null;
        Phase phase = null;
        String description = "Studied for " + request.getMinutes() + " minutes";

        if (request.getTopicId() != null) {
            topic = topicRepository.findById(request.getTopicId()).orElse(null);
            if (topic != null) {
                phase = topic.getPhase();
                description = "Studied: " + topic.getTitle() + " (" + request.getMinutes() + " min)";
            }
        }

        Activity activity = Activity.builder()
                .topic(topic)
                .phase(phase)
                .activityType(ActivityType.STUDY_LOGGED)
                .description(description)
                .minutesSpent(request.getMinutes())
                .xpEarned(0)
                .activityDate(date)
                .createdAt(LocalDateTime.now())
                .build();

        activityRepository.save(activity);

        // Update daily log
        DailyLog dailyLog = dailyLogRepository.findByLogDate(date)
                .orElse(DailyLog.builder()
                        .logDate(date)
                        .totalMinutes(0)
                        .topicsCompleted(0)
                        .xpEarned(0)
                        .createdAt(LocalDateTime.now())
                        .build());

        dailyLog.setTotalMinutes(dailyLog.getTotalMinutes() + request.getMinutes());
        dailyLogRepository.save(dailyLog);

        log.info("Study time logged: {} minutes on {}", request.getMinutes(), date);
        return toActivityDTO(activity);
    }

    /**
     * Generate heatmap data for the last 365 days.
     * Returns entries for every day (including zero-activity days).
     */
    @Transactional(readOnly = true)
    public List<HeatmapEntryDTO> getHeatmapData() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(364);

        // Get all daily logs in range
        List<DailyLog> logs = dailyLogRepository.findByDateRange(startDate, endDate);

        // Build a map for quick lookup
        Map<LocalDate, DailyLog> logMap = logs.stream()
                .collect(Collectors.toMap(DailyLog::getLogDate, d -> d));

        // Generate entries for every day
        List<HeatmapEntryDTO> heatmap = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DailyLog log = logMap.get(date);
            heatmap.add(HeatmapEntryDTO.builder()
                    .date(date)
                    .minutes(log != null ? log.getTotalMinutes() : 0)
                    .topicsCompleted(log != null ? log.getTopicsCompleted() : 0)
                    .xpEarned(log != null ? log.getXpEarned() : 0)
                    .build());
        }

        return heatmap;
    }

    // ── Mapper ─────────────────────────────────────────

    private ActivityDTO toActivityDTO(Activity activity) {
        return ActivityDTO.builder()
                .id(activity.getId())
                .topicTitle(activity.getTopic() != null ? activity.getTopic().getTitle() : null)
                .phaseTitle(activity.getPhase() != null ? activity.getPhase().getTitle() : null)
                .phaseNumber(activity.getPhase() != null ? activity.getPhase().getPhaseNumber() : -1)
                .activityType(activity.getActivityType().name())
                .description(activity.getDescription())
                .minutesSpent(activity.getMinutesSpent())
                .xpEarned(activity.getXpEarned())
                .activityDate(activity.getActivityDate())
                .createdAt(activity.getCreatedAt())
                .build();
    }
}
