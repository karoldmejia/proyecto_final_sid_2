package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.IRoleService;
import com.example.physical_activity_project.services.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;


@Controller
@RequestMapping("mvc/signup")
@RequiredArgsConstructor
public class SignUpMVCController {

    private final IUserService userService;
    private final IRoleService roleService;

    @GetMapping("/form")
    public String showSignUpPage(@RequestParam(value = "error", required = false) String error, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        if(error != null) {
            model.addAttribute("error", "An account with this email already exists.");
        }
        return "login/login";
    }

    @PostMapping("/form")
    public String signup(@ModelAttribute User user, Model model) {
        try {
            // Verificar si ya existe un usuario
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return "redirect:/mvc/login?error=true";
            }

            // Asignar rol por defecto (User)
            Role userRole = roleService.getRoleByName("Admin")
                    .orElseThrow(() -> new RuntimeException("Rol 'User' no encontrado"));

            user.setRole(userRole);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));

            userService.save(user);
            return "redirect:/mvc/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear la cuenta: " + e.getMessage());
            model.addAttribute("user", user);
            return "login/login";
        }
    }
}
