package com.example.physical_activity_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "routines")
public class Routine {

    @Id
    private ObjectId id;

    @Field("usuario_sql_id")
    private Long userSqlId;

    @Field("origen_publico_id")
    private ObjectId originPublicId;

    @Field("name")
    private String name;

    @Field("creation_date")
    private Date creationDate;

    private List<RoutineExercise> exercises;
}
