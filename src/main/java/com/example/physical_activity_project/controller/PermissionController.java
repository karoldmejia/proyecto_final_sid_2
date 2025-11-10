package com.example.physical_activity_project.controller;

import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.services.impl.PermissionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    @Autowired
    private PermissionServiceImpl permissionService;

    // Obtener todos
    @GetMapping
    public List<Permission> getAllPermissions() {
        return permissionService.findAll();
    }

    // Obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear permiso
    @PostMapping
    public Permission createPermission(@RequestBody Permission permission) {
        return permissionService.save(permission);
    }

    // Actualizar permiso
    @PutMapping("/{id}")
    public ResponseEntity<Permission> updatePermission(@PathVariable Long id, @RequestBody Permission permission) {
        try {
            return ResponseEntity.ok(permissionService.update(id, permission));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Borrar permiso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
