package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);
    User save(User user);
    void deleteById(String id);
    List<User> findByRole(Role role);
}

