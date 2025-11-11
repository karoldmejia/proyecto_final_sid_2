package com.example.physical_activity_project.dto;

import lombok.Data;
import java.util.Date;

@Data
public class ProgressSnapshotDTO {
    private Date progressDate;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer timeCompleted;
    private Integer effortLevel;
}
