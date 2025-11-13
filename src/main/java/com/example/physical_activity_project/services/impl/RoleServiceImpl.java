package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.*;
import com.example.physical_activity_project.repository.IRolePermissionRepository;
import com.example.physical_activity_project.repository.IRoleRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.repository.IUserRoleRepository;
import com.example.physical_activity_project.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final IRoleRepository roleRepository;
    private final IRolePermissionRepository rolePermissionRepository;
    private final IUserRepository userRepository;
    private final IUserRoleRepository userRoleRepository;

    @Transactional(readOnly = true)
    public List<Permission> getPermissions(Long roleId) {
        return roleRepository.findPermissionsByRoleId(roleId);
    }

    @Transactional
    public Role save(Role role, List<Permission> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            throw new RuntimeException("Todo rol debe tener al menos un permiso asignado");
        }
        Role savedRole = roleRepository.save(role);
        rolePermissionRepository.deleteAllByRole(savedRole);
        for (Permission permission : permissions) {
            RolePermission rp = new RolePermission();
            rp.setRole(savedRole);
            rp.setPermission(permission);
            rolePermissionRepository.save(rp);
        }
        return savedRole;
    }


    @Transactional
    public Role update(Long roleId, Role roleDetails, List<Permission> permissions) {
        Role existingRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        existingRole.setName(roleDetails.getName());
        existingRole.setDescription(roleDetails.getDescription());
        return save(existingRole, permissions);
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    public void deleteById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        if ("User".equalsIgnoreCase(role.getName())) {
            throw new RuntimeException("¡El rol User no puede ser eliminado!");
        }

        Role userRole = roleRepository.findByName("User")
                .orElseThrow(() -> new RuntimeException("Rol 'User' no encontrado"));

        List<UserRole> affectedUserRoles = userRoleRepository.findByRole(role);
        for (UserRole ur : affectedUserRoles) {
            userRoleRepository.delete(ur);

            UserRole newUserRole = new UserRole();
            newUserRole.setUser(ur.getUser());
            newUserRole.setRole(userRole);
            userRoleRepository.save(newUserRole);
        }

        rolePermissionRepository.deleteAllByRole(role);
        roleRepository.delete(role);
    }


    @Transactional
    public Role addPermissionToRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        boolean exists = rolePermissionRepository.findByRoleAndPermission(role, permission).isPresent();
        if (exists) {
            throw new RuntimeException("El permiso ya está asignado a este rol");
        }
        RolePermission rp = new RolePermission();
        rp.setRole(role);
        rp.setPermission(permission);
        rolePermissionRepository.save(rp);

        return role;
    }

    @Transactional
    public Role removePermissionFromRole(Long roleId, Permission permission) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        // Contar cuántos permisos tiene el rol
        long totalPermisos = rolePermissionRepository.countByRole(role);

        if (totalPermisos <= 1) {
            throw new RuntimeException(
                    "No se puede eliminar el último permiso del rol. Asigne otro permiso antes o elimine el rol."
            );
        }
        RolePermission rp = rolePermissionRepository.findByRoleAndPermission(role, permission)
                .orElseThrow(() -> new RuntimeException("El permiso no está asignado a este rol"));
        rolePermissionRepository.delete(rp);
        return role;
    }

}
