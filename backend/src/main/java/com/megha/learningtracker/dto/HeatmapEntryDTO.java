package com.megha.learningtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeatmapEntryDTO {
    private LocalDate date;
    private int minutes;
    private int topicsCompleted;
    private int xpEarned;
}
