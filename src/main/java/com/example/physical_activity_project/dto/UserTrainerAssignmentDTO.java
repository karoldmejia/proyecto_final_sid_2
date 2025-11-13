package com.example.physical_activity_project.dto;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class UserTrainerAssignmentDTO {

    private Long id;
    private Timestamp assignmentDate;
    private String status;

    private String trainerId;
    private String userId;

    private String trainerName;
    private String userName;
}
