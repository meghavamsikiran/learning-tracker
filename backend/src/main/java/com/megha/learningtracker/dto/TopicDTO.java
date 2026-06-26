package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopicDTO {
    private Long id;
    private Long phaseId;
    private String phaseTitle;
    private int phaseNumber;
    private String title;
    private String description;
    private int estimatedMinutes;
    private String status;
    private String notes;
    private int xpReward;
    private LocalDateTime completedAt;
    private int sortOrder;
}
