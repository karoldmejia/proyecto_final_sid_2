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
@Document(collection = "exercise_progress")
public class ExerciseProgress {

    @Id
    private ObjectId id;

    @Field("progress_date")
    private Date progressDate;

    @Field("sets_completed")
    private Integer setsCompleted;

    @Field("reps_completed")
    private Integer repsCompleted;

    @Field("time_completed")
    private Integer timeCompleted;

    @Field("effort_level")
    private Integer effortLevel;

    // Solo referencia al usuario
    @Field("user_id")
    private Long userId;

    @Field("routine_exercise_id")
    private ObjectId routineExerciseId;

    @Field("recommendations")
    private List<Recommendation> recommendations;
}
