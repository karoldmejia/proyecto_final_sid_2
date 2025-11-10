package com.example.physical_activity_project.controller.mvc;


import com.example.physical_activity_project.model.Permission;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.services.IPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/mvc/permissions")
@RequiredArgsConstructor
public class PermissionMVCController {

    private final IPermissionService permissionService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("permissions", permissionService.findAll());
        return "admin/permissions/list";
    }

    @GetMapping("/add")
    public String addPermissionForm(Model model) {
        Permission permission = new Permission();
        model.addAttribute("permission", permission);
        return "admin/permissions/add";
    }

    @PostMapping("/add")
    public String addPermission(@ModelAttribute Permission permission) {
        permissionService.save(permission);
        return "redirect:/mvc/permissions";
    }

    @GetMapping("/edit")
    public String editPermissionForm(@RequestParam Long id, Model model) {
        Permission permission = permissionService.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        model.addAttribute("actualPermission", permission);
        return "admin/permissions/edit";
    }

    @PostMapping("/edit")
    public String editPermission(@ModelAttribute("actualPermission") Permission permission) {
        permissionService.save(permission);
        return "redirect:/mvc/permissions";
    }

    @GetMapping("/delete")
    public String deletePermission(@RequestParam Long id, Model model) {
        try {
            permissionService.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            model.addAttribute("error", "No se puede eliminar el permiso porque está siendo usado por algún rol.");
            model.addAttribute("permissions", permissionService.findAll()); // volver a cargar la lista
            return "admin/permissions/list"; // regresar a la lista mostrando el error
        }
        return "redirect:/mvc/permissions";
    }


}
