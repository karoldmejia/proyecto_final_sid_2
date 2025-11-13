package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Routine;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRoutineRepository extends MongoRepository<Routine, ObjectId> {
    List<Routine> findByUserSqlId(String userSqlId);
    Optional<Routine> findByExercises_ExerciseId(ObjectId exerciseId);
    List<Routine> findByUserSqlIdAndOriginPublicIdNotNull(String userSqlId);
    List<Routine> findByUserSqlIdInAndOriginPublicIdIsNull(List<String> userSqlIds);
}
