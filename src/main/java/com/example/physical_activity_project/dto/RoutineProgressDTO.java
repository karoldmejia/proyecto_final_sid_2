package com.example.physical_activity_project.dto;

import lombok.Data;

@Data
public class RoutineProgressDTO {
    private String routineName;
    private int totalExercises;   // número de ejercicios completados
    private int totalSets;
    private int totalReps;
    private int totalTime;
    private double avgEffort;
}
