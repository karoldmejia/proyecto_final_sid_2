package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.UserTrainerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IUserTrainerAssignmentRepository extends JpaRepository<UserTrainerAssignment, Long>{
    List<UserTrainerAssignment> findByTrainerUsername(String username);
    List<UserTrainerAssignment> findByUserUsername(String username);
    Optional<UserTrainerAssignment> findByTrainerUsernameAndUserUsernameAndStatus(String trainerId, String userId, String status);
}
