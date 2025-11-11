package com.example.physical_activity_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExercise {

    private ObjectId exerciseId;
    private String nameSnapshot;
    private Integer sets;
    private Integer reps;
    private Integer time;
}
