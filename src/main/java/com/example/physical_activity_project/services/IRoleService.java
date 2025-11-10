package com.example.physical_activity_project.services;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    Role save(Role role, List<Permission> permissions);
    List<Role> getAllRoles();
    Optional<Role> getRoleById(Long id);
    Optional<Role> getRoleByName(String name);
    void deleteById(Long id);
    public Role addPermissionToRole(Long roleId, Permission permission);
    public Role removePermissionFromRole(Long roleId, Permission permission);
}
