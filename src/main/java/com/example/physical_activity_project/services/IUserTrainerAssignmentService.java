package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.UserTrainerAssignment;

import java.util.List;

public interface IUserTrainerAssignmentService {
        UserTrainerAssignment assignTrainerToUser(String trainerId, String userId);
    UserTrainerAssignment updateAssignmentStatus(Long assignmentId, String newStatus);
    List<UserTrainerAssignment> getAssignmentsByTrainer(String trainerId);
    List<UserTrainerAssignment> getAssignmentsByUser(String userId);
    void deleteAssignment(Long assignmentId);
}
