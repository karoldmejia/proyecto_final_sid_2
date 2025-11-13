package com.example.physical_activity_project.services;

import java.util.List;
import java.util.Optional;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.Role;

public interface IUserService {
    public User save(User user);
    public List<User> getAllUsers();
    public Optional<User> getUserById(String id);
    public void initializedUsers();
    void deleteById(String id);
    Optional<User> findByUsername(String username);
    public List<User> getUsersByRoleName(String roleName);

}
