package com.megha.learningtracker.service;

import org.springframework.stereotype.Service;

@Service
public class XpService {

    private static final int XP_PER_LEVEL = 500;
    private static final int TOPIC_COMPLETE_XP = 10;
    private static final int PHASE_COMPLETE_BONUS = 50;

    /**
     * Calculate the level based on total XP.
     * Level 1 starts at 0 XP, level up every 500 XP.
     */
    public int calculateLevel(int totalXp) {
        return (totalXp / XP_PER_LEVEL) + 1;
    }

    /**
     * XP remaining until next level.
     */
    public int xpToNextLevel(int totalXp) {
        return XP_PER_LEVEL - (totalXp % XP_PER_LEVEL);
    }

    /**
     * XP earned for completing a topic.
     */
    public int getTopicCompletionXp() {
        return TOPIC_COMPLETE_XP;
    }

    /**
     * Bonus XP for completing an entire phase.
     */
    public int getPhaseCompletionBonus() {
        return PHASE_COMPLETE_BONUS;
    }
}
