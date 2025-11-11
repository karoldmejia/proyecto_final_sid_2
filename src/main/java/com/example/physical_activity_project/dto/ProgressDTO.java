package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDTO {
    private Long totalExercises;
    private Integer totalSets;
    private Integer totalReps;
    private Integer totalTime;
    private Double averageEffort;
}
