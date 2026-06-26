package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicUpdateRequest {
    private String status;
    private String notes;
    private Integer minutesSpent;
}
