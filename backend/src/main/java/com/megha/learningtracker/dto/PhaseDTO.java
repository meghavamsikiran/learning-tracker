package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhaseDTO {
    private Long id;
    private int phaseNumber;
    private String title;
    private String description;
    private String icon;
    private int totalTopics;
    private int completedTopics;
    private String status;
    private double progressPercentage;
    private List<TopicDTO> topics;
}
