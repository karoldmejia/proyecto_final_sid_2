package com.example.physical_activity_project.controller.mvc;


import com.example.physical_activity_project.dto.RoleDTO;
import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.services.IPermissionService;
import com.example.physical_activity_project.services.IRolePermissionService;
import com.example.physical_activity_project.services.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mvc/roles")
@RequiredArgsConstructor
public class RolMVCController {

    private final IRoleService roleService;
    private final IPermissionService permissionService;
    private final IRolePermissionService rolePermissionService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/roles/list";
    }

    @GetMapping("/add")
    public String addRolForm(Model model) {
        Role role = new Role();
        model.addAttribute("role", role);
        model.addAttribute("allPermissions", permissionService.findAll());
        return "admin/roles/add";
    }

    @PostMapping("/add")
    public String addRole(
            @ModelAttribute Role role,
            @RequestParam(value = "permissions", required = false) List<Long> permissionIds,
            Model model) {

        List<Permission> permissions = (permissionIds != null)
                ? permissionService.getPermissionsByIds(permissionIds)
                : List.of();

        try {
            roleService.save(role, permissions);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("role", role);
            model.addAttribute("allPermissions", permissionService.findAll());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/roles/list";
        }

        return "redirect:/mvc/roles";
    }

    @GetMapping("/edit")
    public String editRoleForm(@RequestParam Long id, Model model) {
        Optional<Role> role = roleService.getRoleById(id);
        if(role.isEmpty()) {
            return "redirect:/mvc/roles"; // si no existe, redirigir
        }

        RoleDTO form = new RoleDTO();
        form.setId(role.get().getId());
        form.setName(role.get().getName());
        form.setDescription(role.get().getDescription());

        List<Long> permisoIds = rolePermissionService.findPermissionIdsByRoleId(id);
        List<Permission> permisos = permissionService.getPermissionsByIds(permisoIds);
        form.setPermissions(permisos);

        model.addAttribute("form", form);
        model.addAttribute("allPermissions", permissionService.findAll());
        return "admin/roles/edit";
    }


    @PostMapping("/edit")
    public String editRole(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "permissions", required = false) List<Long> permissionIds,
            Model model) {

        Role role = new Role();
        role.setId(id);
        role.setName(name);
        role.setDescription(description);

        List<Permission> permissions = (permissionIds != null)
                ? permissionService.getPermissionsByIds(permissionIds)
                : List.of();

        try {
            roleService.save(role, permissions);
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("role", role);
            model.addAttribute("allPermissions", permissionService.findAll());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/roles/list";
        }

        return "redirect:/mvc/roles";
    }

    @GetMapping("/delete")
    public String deleteRole(@RequestParam Long id, Model model) {
        try {
            roleService.deleteById(id);
            return "redirect:/mvc/roles";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/roles/list";
        }
    }
}
