package com.example.physical_activity_project.dto;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class ExerciseDTO {

    private ObjectId id;
    private String name;
    private String type;
    private String description;
    private Double duration;
    private String difficulty;
    private String videoUrl;
    private String category;
}
