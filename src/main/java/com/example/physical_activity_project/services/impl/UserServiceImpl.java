package com.example.physical_activity_project.services.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.physical_activity_project.repository.IRoleRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final IRoleRepository roleRepository;

    // Crear o actualizar usuario
    public User save(User user) {
        if (user.getRole() == null) {
            throw new RuntimeException("Usuario debe tener un rol asignado");
        }
        // Codificar contraseña si no está codificada
        if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user); // sirve para crear o actualizar
    }

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener usuario por ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Eliminar usuario
    public void deleteById(Long id) {
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
    public User changeUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (newRole == null) {
            throw new RuntimeException("El nuevo rol no puede ser nulo");
        }
        user.setRole(newRole);
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByRoleName(String roleName) {

        // 1. Buscar el rol por su nombre en la base de datos.
        Optional<Role> roleOpt = roleRepository.findByName(roleName);

        // 2. Comprobar si el rol existe.
        if (roleOpt.isEmpty()) {
            // Si el rol (ej. "TRAINER") no está en la tabla 'roles',
            // devolvemos una lista vacía para evitar errores.
            System.err.println("Advertencia: El rol '" + roleName + "' no fue encontrado.");
            return Collections.emptyList();
        }

        // 3. Si el rol existe, buscar a todos los usuarios que tengan ese rol.
        //    Esto asume que tu UserRepository tiene un método findByRolesContains
        //    y que tu entidad User tiene un campo como: Set<Role> roles.
        return userRepository.findByRole(roleOpt.get());
    }
}

