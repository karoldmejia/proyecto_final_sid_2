package com.example.physical_activity_project.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.physical_activity_project.model.UserRole;
import com.example.physical_activity_project.repository.IRoleRepository;
import com.example.physical_activity_project.repository.IUserRoleRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    // Crear o actualizar usuario
    public User save(User user) {
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        User savedUser = userRepository.save(user);

        if (user.getRole() != null) {
            Role roleEntity = mapRole(user);
            boolean alreadyAssigned = userRoleRepository.existsByUserAndRole(savedUser, roleEntity);
            if (!alreadyAssigned) {
                UserRole userRole = new UserRole();
                userRole.setUser(savedUser);
                userRole.setRole(roleEntity);
                userRoleRepository.save(userRole);
            }
        }

        return savedUser;
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener usuario por ID
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    // Eliminar usuario
    public void deleteById(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public void initializedUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
            }
        }
    }

    // Cambiar el rol de un usuario

    private Role mapRole(User user) {
        String dbRole = user.getRole();

        switch (dbRole.toUpperCase()) {
            case "EMPLOYEE":
                return roleRepository.findByName("Trainer")
                        .orElseThrow(() -> new RuntimeException("Rol 'Trainer' no encontrado"));
            case "STUDENT":
                return roleRepository.findByName("User")
                        .orElseThrow(() -> new RuntimeException("Rol 'User' no encontrado"));
            case "ADMIN":
                return roleRepository.findByName("Admin")
                        .orElseThrow(() -> new RuntimeException("Rol 'Admin' no encontrado"));
            default:
                throw new RuntimeException("Rol desconocido: " + dbRole);
        }
    }

    public boolean hasRole(User user, String roleName) {
        if (user == null || roleName == null) return false;

        List<UserRole> roles = userRoleRepository.findByUser(user);
        return roles.stream()
                .anyMatch(ur -> roleName.equalsIgnoreCase(ur.getRole().getName()));
    }

    // Versión sobre ID del usuario
    public boolean hasRole(String userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        return hasRole(user, roleName);
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

        List<UserRole> userRoles = userRoleRepository.findByRole(role);

        return userRoles.stream()
                .map(UserRole::getUser)
                .toList();
    }
}

