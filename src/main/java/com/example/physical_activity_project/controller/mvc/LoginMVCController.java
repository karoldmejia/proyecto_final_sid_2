package com.example.physical_activity_project.controller.mvc;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mvc/login")
@RequiredArgsConstructor
public class LoginMVCController {

    @GetMapping
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model) {
        if(error != null) {
            model.addAttribute("error", "Invalid username or password.");
        }
        if(logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login/login";
    }

}
