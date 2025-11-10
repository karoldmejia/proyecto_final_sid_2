package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.UserTrainerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IUserTrainerAssignmentRepository extends JpaRepository<UserTrainerAssignment, Long>{
    List<UserTrainerAssignment> findByTrainerId(Long trainerId);
    List<UserTrainerAssignment> findByUserId(Long userId);
    Optional<UserTrainerAssignment> findByTrainerIdAndUserIdAndStatus(Long trainerId, Long userId, String status);
}
