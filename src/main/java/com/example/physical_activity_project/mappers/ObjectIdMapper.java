package com.example.physical_activity_project.mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ObjectIdMapper {

    public String objectIdToString(ObjectId objectId) {
        return objectId != null ? objectId.toString() : null;
    }

    public ObjectId stringToObjectId(String string) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        try {
            return new ObjectId(string);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}