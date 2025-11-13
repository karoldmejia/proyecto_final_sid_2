package com.example.physical_activity_project.dto;

import lombok.Data;
import org.bson.types.ObjectId;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
public class RoutineDTO {
    private ObjectId id;
    private String userSqlId;
    private ObjectId originPublicId;
    private String name;
    private Date creationDate;
    private List<RoutineExerciseDTO> exercises;
}
