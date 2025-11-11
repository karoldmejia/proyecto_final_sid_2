package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Exercise;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExerciseRepository extends MongoRepository<Exercise, ObjectId> {
    List<Exercise> findByType(String type);
    List<Exercise> findByDifficulty(String difficulty);
}