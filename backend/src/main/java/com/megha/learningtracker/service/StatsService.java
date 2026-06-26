package com.megha.learningtracker.service;

import com.megha.learningtracker.dto.StatsDTO;
import com.megha.learningtracker.repository.DailyLogRepository;
import com.megha.learningtracker.repository.PhaseRepository;
import com.megha.learningtracker.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final TopicRepository topicRepository;
    private final PhaseRepository phaseRepository;
    private final DailyLogRepository dailyLogRepository;
    private final StreakService streakService;
    private final XpService xpService;

    @Transactional(readOnly = true)
    public StatsDTO getStats() {
        int completedTopics = topicRepository.countCompletedTopics();
        int totalTopics = topicRepository.countTotalTopics();
        int completedPhases = phaseRepository.countCompletedPhases();
        int totalPhases = (int) phaseRepository.count();
        int totalMinutes = dailyLogRepository.sumTotalMinutes();
        int activeDays = dailyLogRepository.countActiveDays();
        int totalXp = topicRepository.sumEarnedXp();

        // Add phase completion bonuses
        totalXp += completedPhases * xpService.getPhaseCompletionBonus();

        int currentStreak = streakService.getCurrentStreak();
        int longestStreak = streakService.getLongestStreak();
        int level = xpService.calculateLevel(totalXp);
        int xpToNext = xpService.xpToNextLevel(totalXp);

        double completionPct = totalTopics > 0
                ? (double) completedTopics / totalTopics * 100 : 0;
        double avgMinPerDay = activeDays > 0
                ? (double) totalMinutes / activeDays : 0;

        // Predict remaining days based on current pace
        int remainingTopics = totalTopics - completedTopics;
        double topicsPerDay = activeDays > 0
                ? (double) completedTopics / activeDays : 0;
        int predictedDays = topicsPerDay > 0
                ? (int) Math.ceil(remainingTopics / topicsPerDay) : -1;

        LocalDate estimatedCompletion = predictedDays > 0
                ? LocalDate.now().plusDays(predictedDays) : null;

        return StatsDTO.builder()
                .totalXp(totalXp)
                .level(level)
                .xpToNextLevel(xpToNext)
                .totalStudyMinutes(totalMinutes)
                .totalStudyHours(Math.round(totalMinutes / 60.0 * 10.0) / 10.0)
                .totalTopicsCompleted(completedTopics)
                .totalTopics(totalTopics)
                .completionPercentage(Math.round(completionPct * 10.0) / 10.0)
                .currentStreak(currentStreak)
                .longestStreak(longestStreak)
                .phasesCompleted(completedPhases)
                .totalPhases(totalPhases)
                .avgMinutesPerDay(Math.round(avgMinPerDay * 10.0) / 10.0)
                .activeDays(activeDays)
                .predictedDaysRemaining(predictedDays)
                .estimatedCompletionDate(estimatedCompletion)
                .build();
    }
}
