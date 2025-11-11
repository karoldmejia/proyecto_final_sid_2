package com.example.physical_activity_project.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;

@Data
public class RoutineExerciseDTO {
    private ObjectId exerciseId;
    private String nameSnapshot;
    private Integer sets;
    private Integer reps;
    private Integer time;

    private List<ProgressSnapshotDTO> recentProgresses;
}
