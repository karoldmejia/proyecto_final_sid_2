package com.example.physical_activity_project.controller.mvc;


import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.IRoleService;
import com.example.physical_activity_project.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@Controller
@RequestMapping("/mvc/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')")
public class UserMVCController {

    private final IUserService userService;
    private final IRoleService roleService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
        return "admin/users/list";
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin/users/add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user, @RequestParam("role") Long roleId) {
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        Role role = roleService.getRoleById(roleId).orElse(null);
        user.setRole(role);
        userService.save(user);
        return "redirect:/mvc/users";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam Long id, Model model) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("actualUser", userOpt.get());
            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/users/edit";
        } else {
            model.addAttribute("actualUser", null);
            return "admin/users/edit";
        }
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("actualUser") User user, @RequestParam("role") Long roleId) {
        Optional<User> existingUserOpt = userService.getUserById(user.getId());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Actualizar campos básicos
            existingUser.setUsername(user.getUsername());
            // Actualizar rol
            Role role = roleService.getRoleById(roleId).orElse(null);
            existingUser.setRole(role);

            userService.save(existingUser);
        }

        return "redirect:/mvc/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam Long id, Model model) {
        try {
            userService.deleteById(id);
            return "redirect:/mvc/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            return "admin/users/list";
        }
    }

}