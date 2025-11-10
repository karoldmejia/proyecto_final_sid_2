package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.UserTrainerAssignment;

import java.util.List;

public interface IUserTrainerAssignmentService {
    UserTrainerAssignment assignTrainerToUser(Long trainerId, Long userId);
    UserTrainerAssignment updateAssignmentStatus(Long assignmentId, String newStatus);
    List<UserTrainerAssignment> getAssignmentsByTrainer(Long trainerId);
    List<UserTrainerAssignment> getAssignmentsByUser(Long userId);
    void deleteAssignment(Long assignmentId);
}
