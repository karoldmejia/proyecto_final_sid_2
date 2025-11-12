package com.example.physical_activity_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatisticsDTO {
    private Long entityId;
    private Integer year;
    private Integer month;

    private Integer routinesStarted;
    private Integer userRecommendations;
    private Integer newAssignments;
    private Integer trainerFollowups;
}
