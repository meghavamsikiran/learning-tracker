package com.megha.learningtracker.service;

import com.megha.learningtracker.dto.PhaseDTO;
import com.megha.learningtracker.dto.TopicDTO;
import com.megha.learningtracker.dto.TopicUpdateRequest;
import com.megha.learningtracker.entity.*;
import com.megha.learningtracker.exception.ResourceNotFoundException;
import com.megha.learningtracker.repository.ActivityRepository;
import com.megha.learningtracker.repository.DailyLogRepository;
import com.megha.learningtracker.repository.PhaseRepository;
import com.megha.learningtracker.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurriculumService {

    private final PhaseRepository phaseRepository;
    private final TopicRepository topicRepository;
    private final ActivityRepository activityRepository;
    private final DailyLogRepository dailyLogRepository;
    private final XpService xpService;

    /**
     * Get all phases with their topics and progress.
     */
    @Transactional(readOnly = true)
    public List<PhaseDTO> getAllPhases() {
        return phaseRepository.findAllByOrderByPhaseNumberAsc().stream()
                .map(this::toPhaseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single phase with all its topics.
     */
    @Transactional(readOnly = true)
    public PhaseDTO getPhaseById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phase", id));
        return toPhaseDTO(phase);
    }

    /**
     * Get the current active phase (first non-completed phase).
     */
    @Transactional(readOnly = true)
    public PhaseDTO getCurrentPhase() {
        return phaseRepository.findAllByOrderByPhaseNumberAsc().stream()
                .filter(p -> p.getStatus() != TopicStatus.COMPLETED)
                .findFirst()
                .map(this::toPhaseDTO)
                .orElse(null);
    }

    /**
     * Update a topic's status and/or notes.
     */
    @Transactional
    public TopicDTO updateTopic(Long topicId, TopicUpdateRequest request) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", topicId));

        if (request.getStatus() != null) {
            TopicStatus newStatus = TopicStatus.valueOf(request.getStatus());
            topic.setStatus(newStatus);

            if (newStatus == TopicStatus.IN_PROGRESS) {
                // Log topic started activity
                logActivity(topic, ActivityType.TOPIC_STARTED,
                        "Started: " + topic.getTitle(), 0, 0);
            }
        }

        if (request.getNotes() != null) {
            topic.setNotes(request.getNotes());
        }

        topicRepository.save(topic);
        updatePhaseProgress(topic.getPhase());

        return toTopicDTO(topic);
    }

    /**
     * Mark a topic as completed: update status, log activity, award XP, update daily log.
     */
    @Transactional
    public TopicDTO completeTopic(Long topicId, Integer minutesSpent) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic", topicId));

        if (topic.getStatus() == TopicStatus.COMPLETED) {
            log.warn("Topic {} is already completed", topicId);
            return toTopicDTO(topic);
        }

        // Mark topic as completed
        topic.setStatus(TopicStatus.COMPLETED);
        topic.setCompletedAt(LocalDateTime.now());
        topicRepository.save(topic);

        int xpEarned = topic.getXpReward();
        int minutes = minutesSpent != null ? minutesSpent : topic.getEstimatedMinutes();

        // Log activity
        logActivity(topic, ActivityType.TOPIC_COMPLETED,
                "Completed: " + topic.getTitle(), minutes, xpEarned);

        // Update daily log
        updateDailyLog(LocalDate.now(), minutes, 1, xpEarned);

        // Update phase progress
        Phase phase = topic.getPhase();
        updatePhaseProgress(phase);

        // Check if entire phase is now completed
        if (phase.getCompletedTopics() == phase.getTotalTopics()) {
            phase.setStatus(TopicStatus.COMPLETED);
            phaseRepository.save(phase);

            int bonusXp = xpService.getPhaseCompletionBonus();
            logActivity(topic, ActivityType.PHASE_COMPLETED,
                    "Phase " + phase.getPhaseNumber() + " completed: " + phase.getTitle(),
                    0, bonusXp);
            updateDailyLog(LocalDate.now(), 0, 0, bonusXp);
        }

        log.info("Topic completed: {} (XP: {})", topic.getTitle(), xpEarned);
        return toTopicDTO(topic);
    }

    // ── Private Helpers ────────────────────────────────

    private void updatePhaseProgress(Phase phase) {
        int completedCount = topicRepository.countCompletedByPhaseId(phase.getId());
        phase.setCompletedTopics(completedCount);

        if (completedCount == 0) {
            phase.setStatus(TopicStatus.NOT_STARTED);
        } else if (completedCount < phase.getTotalTopics()) {
            phase.setStatus(TopicStatus.IN_PROGRESS);
        } else {
            phase.setStatus(TopicStatus.COMPLETED);
        }

        phaseRepository.save(phase);
    }

    private void logActivity(Topic topic, ActivityType type, String description,
                             int minutes, int xp) {
        Activity activity = Activity.builder()
                .topic(topic)
                .phase(topic.getPhase())
                .activityType(type)
                .description(description)
                .minutesSpent(minutes)
                .xpEarned(xp)
                .activityDate(LocalDate.now())
                .createdAt(LocalDateTime.now())
                .build();
        activityRepository.save(activity);
    }

    private void updateDailyLog(LocalDate date, int minutes, int topics, int xp) {
        DailyLog dailyLog = dailyLogRepository.findByLogDate(date)
                .orElse(DailyLog.builder()
                        .logDate(date)
                        .totalMinutes(0)
                        .topicsCompleted(0)
                        .xpEarned(0)
                        .createdAt(LocalDateTime.now())
                        .build());

        dailyLog.setTotalMinutes(dailyLog.getTotalMinutes() + minutes);
        dailyLog.setTopicsCompleted(dailyLog.getTopicsCompleted() + topics);
        dailyLog.setXpEarned(dailyLog.getXpEarned() + xp);
        dailyLogRepository.save(dailyLog);
    }

    // ── Mappers ────────────────────────────────────────

    private PhaseDTO toPhaseDTO(Phase phase) {
        double progressPct = phase.getTotalTopics() > 0
                ? (double) phase.getCompletedTopics() / phase.getTotalTopics() * 100
                : 0;

        List<TopicDTO> topicDTOs = phase.getTopics().stream()
                .map(this::toTopicDTO)
                .collect(Collectors.toList());

        return PhaseDTO.builder()
                .id(phase.getId())
                .phaseNumber(phase.getPhaseNumber())
                .title(phase.getTitle())
                .description(phase.getDescription())
                .icon(phase.getIcon())
                .totalTopics(phase.getTotalTopics())
                .completedTopics(phase.getCompletedTopics())
                .status(phase.getStatus().name())
                .progressPercentage(Math.round(progressPct * 10.0) / 10.0)
                .topics(topicDTOs)
                .build();
    }

    private TopicDTO toTopicDTO(Topic topic) {
        return TopicDTO.builder()
                .id(topic.getId())
                .phaseId(topic.getPhase().getId())
                .phaseTitle(topic.getPhase().getTitle())
                .phaseNumber(topic.getPhase().getPhaseNumber())
                .title(topic.getTitle())
                .description(topic.getDescription())
                .estimatedMinutes(topic.getEstimatedMinutes())
                .status(topic.getStatus().name())
                .notes(topic.getNotes())
                .xpReward(topic.getXpReward())
                .completedAt(topic.getCompletedAt())
                .sortOrder(topic.getSortOrder())
                .build();
    }
}
