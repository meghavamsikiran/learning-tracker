package com.megha.learningtracker.service;

import com.megha.learningtracker.entity.DailyLog;
import com.megha.learningtracker.repository.DailyLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final DailyLogRepository dailyLogRepository;

    /**
     * Calculates the current consecutive-day streak.
     * A streak counts backwards from today (or yesterday if no activity today yet).
     */
    public int getCurrentStreak() {
        List<DailyLog> logs = dailyLogRepository.findAllOrderByDateDesc();
        if (logs.isEmpty()) return 0;

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        // Check if today has activity — if not, start from yesterday
        DailyLog firstLog = logs.get(0);
        if (!firstLog.getLogDate().equals(expectedDate)) {
            expectedDate = expectedDate.minusDays(1);
            if (!firstLog.getLogDate().equals(expectedDate)) {
                return 0; // No activity today or yesterday — streak broken
            }
        }

        for (DailyLog log : logs) {
            if (log.getLogDate().equals(expectedDate)
                    && (log.getTotalMinutes() > 0 || log.getTopicsCompleted() > 0)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else if (log.getLogDate().equals(expectedDate)) {
                // Day exists but no activity
                break;
            } else {
                break; // Gap found
            }
        }

        return streak;
    }

    /**
     * Calculates the longest ever consecutive-day streak.
     */
    public int getLongestStreak() {
        List<DailyLog> logs = dailyLogRepository.findAllOrderByDateDesc();
        if (logs.isEmpty()) return 0;

        int longestStreak = 0;
        int currentStreak = 0;
        LocalDate previousDate = null;

        // Process in chronological order
        for (int i = logs.size() - 1; i >= 0; i--) {
            DailyLog log = logs.get(i);

            if (log.getTotalMinutes() <= 0 && log.getTopicsCompleted() <= 0) {
                currentStreak = 0;
                previousDate = log.getLogDate();
                continue;
            }

            if (previousDate == null || log.getLogDate().equals(previousDate.plusDays(1))) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }

            longestStreak = Math.max(longestStreak, currentStreak);
            previousDate = log.getLogDate();
        }

        return longestStreak;
    }
}
