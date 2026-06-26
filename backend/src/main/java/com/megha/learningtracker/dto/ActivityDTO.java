package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityDTO {
    private Long id;
    private String topicTitle;
    private String phaseTitle;
    private int phaseNumber;
    private String activityType;
    private String description;
    private int minutesSpent;
    private int xpEarned;
    private LocalDate activityDate;
    private LocalDateTime createdAt;
}
