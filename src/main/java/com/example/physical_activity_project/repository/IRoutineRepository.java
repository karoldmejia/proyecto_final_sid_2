package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Routine;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRoutineRepository extends MongoRepository<Routine, ObjectId> {
    List<Routine> findByUserSqlId(String userSqlId);
}
