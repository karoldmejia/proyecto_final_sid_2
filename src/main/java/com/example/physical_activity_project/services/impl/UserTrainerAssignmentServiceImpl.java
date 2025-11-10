package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserTrainerAssignmentRepository;
import com.example.physical_activity_project.services.IUserTrainerAssignmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserTrainerAssignmentServiceImpl implements IUserTrainerAssignmentService {

    @Autowired
    public IUserTrainerAssignmentRepository assignmentRepository;
    @Autowired
    public IUserRepository userRepository;

    @Override
    public UserTrainerAssignment assignTrainerToUser(Long trainerId, Long userId) {
        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Trainer not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserTrainerAssignment assignment = new UserTrainerAssignment();
        assignment.setTrainer(trainer);
        assignment.setUser(user);
        assignment.setStatus("ACTIVE");
        assignment.setAssignmentDate(new Timestamp(System.currentTimeMillis()));

        return assignmentRepository.save(assignment);
    }

    @Override
    public UserTrainerAssignment updateAssignmentStatus(Long assignmentId, String newStatus) {
        UserTrainerAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la asignación con id: " + assignmentId));
        assignment.setStatus(newStatus);
        UserTrainerAssignment saved = assignmentRepository.save(assignment);
        return saved;
    }

    @Override
    public List<UserTrainerAssignment> getAssignmentsByTrainer(Long trainerId) {
        return assignmentRepository.findByTrainerId(trainerId);
    }

    @Override
    public List<UserTrainerAssignment> getAssignmentsByUser(Long userId) {
        return assignmentRepository.findByUserId(userId);
    }

    @Override
    public void deleteAssignment(Long assignmentId) {
        UserTrainerAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró la asignación con id: " + assignmentId));
        assignmentRepository.deleteById(assignmentId);
    }
}