package com.example.physical_activity_project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressSnapshot {
    private Date progressDate;
    private Integer setsCompleted;
    private Integer repsCompleted;
    private Integer timeCompleted;
    private Integer effortLevel;
}