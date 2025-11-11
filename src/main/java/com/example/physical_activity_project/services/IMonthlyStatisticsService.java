package com.example.physical_activity_project.services;

public interface IMonthlyStatisticsService {
    void incrementRoutinesStarted(Long userId);
    void incrementUserRecommendations(Long userId);
    void incrementTrainerRecommendations(Long trainerId);
    void incrementNewAssignments(Long trainerId);
    int getUserRoutinesStarted(Long userId, int year, int month);
    int getUserRecommendationsReceived(Long userId, int year, int month);
    int getTrainerNewAssignments(Long trainerId, int year, int month);
    int getTrainerRecommendationsGiven(Long trainerId, int year, int month);


}
