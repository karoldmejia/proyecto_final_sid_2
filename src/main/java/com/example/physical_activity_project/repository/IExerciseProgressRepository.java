package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.ExerciseProgress;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface IExerciseProgressRepository extends MongoRepository<ExerciseProgress, ObjectId> {

    // Obtener todos los progresos de un usuario
    List<ExerciseProgress> findByUserId(String userId);

    // Obtener todos los progresos de un ejercicio específico dentro de una rutina
    List<ExerciseProgress> findByRoutineExerciseId(ObjectId routineExerciseId);
    @Query("{ 'routineExerciseId': { $in: ?0 } }")
    List<ExerciseProgress> findByRoutineExerciseIds(List<ObjectId> routineExerciseIds);

    // Obtener progresos de un usuario en un rango de fechas
    List<ExerciseProgress> findByUserIdAndProgressDateBetween(String userId, Date start, Date end);
}
