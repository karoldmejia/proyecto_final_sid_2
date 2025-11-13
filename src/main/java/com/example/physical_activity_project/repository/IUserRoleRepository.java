package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByUserAndRole(User user, Role role);

    List<UserRole> findByUser(User user);

    List<UserRole> findByRole(Role role);
}
