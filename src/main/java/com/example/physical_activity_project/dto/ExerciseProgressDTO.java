package com.example.physical_activity_project.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class ExerciseProgressDTO {
    private ObjectId id;
    private LocalDateTime progressDate;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer timeCompleted;
    private Integer effortLevel;
    private String userId;
    private ObjectId routineExerciseId;
    private String exerciseName;
    private String notes;

    private List<RecommendationDTO> recommendations;

}
