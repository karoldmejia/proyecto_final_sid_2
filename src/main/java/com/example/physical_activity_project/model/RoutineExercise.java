package com.example.physical_activity_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExercise {

    @Id
    private ObjectId id = new ObjectId();

    private ObjectId exerciseId;
    private String nameSnapshot;
    private Integer sets;
    private Integer reps;
    private Integer time;

    private List<ProgressSnapshot> recentProgresses  = new LinkedList<>();

    public void addProgress(ProgressSnapshot progress) {
        recentProgresses.add(progress);
        if (recentProgresses.size() > 20) {
            recentProgresses.remove(0);
        }
    }
}
