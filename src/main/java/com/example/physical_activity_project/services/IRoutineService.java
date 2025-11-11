package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.RoutineExercise;
import org.bson.types.ObjectId;

import java.util.List;

public interface IRoutineService {
    Routine createRoutine(Routine routine);
    Routine updateRoutine(ObjectId id, Routine updatedRoutine);
    void deleteRoutine(ObjectId id);
    List<Routine> getRoutinesByUser(String userId);
    List<Routine> getAllRoutines();
    Routine getRoutineById(ObjectId id);
    Routine removeExerciseFromRoutine(ObjectId routineId, ObjectId exerciseId);
    Routine updateExerciseInRoutine(ObjectId routineId, ObjectId exerciseId, RoutineExercise updated);
    Routine addExerciseToRoutine(ObjectId routineId, RoutineExercise newExercise);

}
