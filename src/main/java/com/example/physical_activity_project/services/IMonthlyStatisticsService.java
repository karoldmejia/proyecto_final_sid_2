package com.example.physical_activity_project.services;

public interface IMonthlyStatisticsService {
    void incrementRoutinesStarted(String userId);
    void incrementUserRecommendations(String userId);
    void incrementTrainerRecommendations(String trainerId);
    void incrementNewAssignments(String trainerId);
    int getUserRoutinesStarted(String userId, int year, int month);
    int getUserRecommendationsReceived(String userId, int year, int month);
    int getTrainerNewAssignments(String trainerId, int year, int month);
    int getTrainerRecommendationsGiven(String trainerId, int year, int month);


}
