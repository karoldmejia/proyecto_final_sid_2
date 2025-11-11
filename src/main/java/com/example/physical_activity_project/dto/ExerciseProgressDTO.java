package com.example.physical_activity_project.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;

@Data
public class ExerciseProgressDTO {
    private ObjectId id;
    private Date progressDate;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer timeCompleted;
    private Integer effortLevel;
    private Long userId;
    private ObjectId routineExerciseId;
}
