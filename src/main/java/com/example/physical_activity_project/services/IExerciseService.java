package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Exercise;
import org.bson.types.ObjectId;

import java.util.List;

public interface IExerciseService {
    Exercise createExercise(Exercise exercise);
    Exercise updateExercise(ObjectId id, Exercise updatedExercise);
    void deleteExercise(ObjectId id);
    Exercise getExerciseById(ObjectId id);
    List<Exercise> getAllExercises();
    List<Exercise> getExercisesByType(String type);
    List<Exercise> getExercisesByDifficulty(String difficulty);
}
