package com.example.physical_activity_project.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IUserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Crear o actualizar usuario - VERSIÓN SIMPLIFICADA
    public User save(User user) {
        // Validar que el role tenga un valor permitido
        validateRole(user.getRole());

        // Codificar password si no está ya codificado
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // Guardar solo el usuario (sin UserRole)
        return userRepository.save(user);
    }

    // Validar que el role sea uno de los permitidos
    private void validateRole(String role) {
        if (role == null) {
            throw new RuntimeException("El rol no puede ser nulo");
        }

        // AJUSTA ESTOS VALORES SEGÚN TU CONSTRAINT
        String normalizedRole = role.trim();
        if (!isValidRole(normalizedRole)) {
            throw new RuntimeException("Rol no válido: " + role + ". Roles permitidos: Admin, Trainer, User");
        }
    }

    private boolean isValidRole(String role) {
        // AJUSTA ESTOS VALORES SEGÚN TU CONSTRAINT
        return "Admin".equals(role) || "Trainer".equals(role) || "User".equals(role);
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

    // Estos métodos ya no funcionarán sin UserRole - los mantenemos por compatibilidad
    public boolean hasRole(User user, String roleName) {
        if (user == null || roleName == null) return false;
        // Ahora comparamos directamente con el campo role del User
        return roleName.equalsIgnoreCase(user.getRole());
    }

    // Versión sobre ID del usuario
    public boolean hasRole(String userId, String roleName) {
        User user = userRepository.findById(userId).orElse(null);
        return hasRole(user, roleName);
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {
        // Ahora buscamos directamente por el campo role
        return userRepository.findByRole(roleName);
    }
}