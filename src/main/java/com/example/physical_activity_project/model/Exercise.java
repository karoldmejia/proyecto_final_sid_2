package com.example.physical_activity_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "exercises")
public class Exercise {

    @Id
    private ObjectId id;

    @Field("name")
    private String name;

    @Field("type")
    private String type;

    @Field("description")
    private String description;

    @Field("duration")
    private Double duration;

    @Field("difficulty")
    private String difficulty;

    @Field("video_url")
    private String videoUrl;
}
