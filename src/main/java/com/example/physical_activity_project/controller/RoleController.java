package com.example.physical_activity_project.controller;


import com.example.physical_activity_project.dto.RoleDTO;
import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.services.impl.PermissionServiceImpl;
import com.example.physical_activity_project.services.impl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;
    private final PermissionServiceImpl permissionService;  // Necesario para convertir IDs a Permission

    @GetMapping
    public List<Role> getAll() {
        return roleService.getAllRoles();
    }

    @GetMapping("/{id}")
    public Optional<Role> getById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/name/{name}")
    public Optional<Role> getByName(@PathVariable String name) {
        return roleService.getRoleByName(name);
    }

    @PostMapping
    public Role create(@RequestBody RoleDTO roleDTO) {
        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        // Obtener IDs desde la lista de Permission
        List<Long> permisoIds = roleDTO.getPermissions().stream()
                .map(Permission::getId)
                .toList();

        List<Permission> permisos = permissionService.getPermissionsByIds(permisoIds);

        return roleService.save(role, permisos);
    }


    @PutMapping("/{id}")
    public Role update(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        Role role = new Role();
        role.setId(id);
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        List<Long> permisoIds = roleDTO.getPermissions().stream()
                .map(Permission::getId)
                .toList();

        List<Permission> permisos = permissionService.getPermissionsByIds(permisoIds);
        return roleService.save(role, permisos);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        roleService.deleteById(id);
    }
}
