package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyLogRequest {
    private int minutes;
    private Long topicId;
    private LocalDate date;
}
