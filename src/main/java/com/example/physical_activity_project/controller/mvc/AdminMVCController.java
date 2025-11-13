package com.example.physical_activity_project.controller.mvc;


import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.Role;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/mvc/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('Admin')")
public class AdminMVCController {

    private final IUserService userService;
    private final IRoleService roleService;
    private final IMonthlyStatisticsService  monthlyStatisticsService;
    private final IExerciseService exerciseService;
    private final ExerciseProgressMapper exerciseProgressMapper;
    private final IExerciseProgressService progressService;

    @GetMapping()
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        auth.getAuthorities().forEach(authority -> System.out.println(authority.getAuthority()));
        return "admin/admin-dashboard";
    }

    @GetMapping("/users")
    public String getAll(Model model) {

        List<User> users = userService.getAllUsers();
        model.addAttribute(users);
        model.addAttribute("users", users);
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
    public String addUser(@ModelAttribute User user, @RequestParam("role") String roleName, Model model) {
        // Asignar fecha de creación y estado activo
        user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        user.setIsActive(true);

        // Usar el valor directamente sin conversión: User, Trainer, Admin
        user.setRole(roleName);

        try {
            // Guardar usuario
            userService.save(user);
            return "redirect:/mvc/admin/users";
        } catch (Exception e) {
            // Log del error para debugging
            System.err.println("Error al guardar usuario: " + e.getMessage());
            model.addAttribute("error", "Error al crear el usuario. Verifica que el rol sea válido.");
            model.addAttribute("user", user);
            return "admin/users/add";
        }
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam String id, Model model) {
        Optional<User> userOpt = userService.findByUsername(id);
        if (userOpt.isPresent()) {
            model.addAttribute("actualUser", userOpt.get());

            model.addAttribute("roles", roleService.getAllRoles());
            return "admin/users/edit";
        } else {
            // SUGERENCIA: Es mejor redirigir si el usuario no existe,
            // o pasar un usuario vacío (new User()) para evitar errores en el formulario HTML.
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("actualUser") User user, @RequestParam("role") String roleType) {
        Optional<User> existingUserOpt = userService.findByUsername(user.getUsername());
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Actualizar campos básicos
            existingUser.setUsername(user.getUsername());

            // Actualizar tipo de rol (STUDENT, EMPLOYEE, ADMIN)
            existingUser.setRole(roleType);
            // save() se encarga de mapear a Role real y crear/actualizar UserRole
            userService.save(existingUser);
        }
        return "redirect:/mvc/users";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam String id, Model model) {
        try {
            userService.deleteById(id);
            return "redirect:/mvc/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.getAllUsers());
            return "admin/users/list";
        }
    }
    /** Biblioteca de ejercicios */
    @GetMapping("/exercises")
    public String getExerciseLibrary(Model model) {
        model.addAttribute("exercises", exerciseService.getAllExercises());
        return "exercises/exercises-list";
    }



    /** Registrar progreso */
    @GetMapping("/log-progress")
    public String showLogProgressForm(Model model) {
        model.addAttribute("progressDTO", new ExerciseProgressDTO());
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        return "admin/log-progress";
    }

    @PostMapping("/log-progress")
    public String saveProgress(@ModelAttribute("progressDTO") ExerciseProgressDTO dto,
                               Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();
        ExerciseProgress entity = exerciseProgressMapper.dtoToEntity(dto);
        progressService.registerProgress(user.getUsername(), entity);
        return "redirect:/mvc/admin/my-progress";
    }

    /** Mostrar progreso del admin */
    @GetMapping("/my-progress")
    public String showMyProgress(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();

        List<ExerciseProgressDTO> progressList = progressService.getProgressByUser(user.getUsername())
                .stream()
                .map(exerciseProgressMapper::entityToDto)
                .toList();

        model.addAttribute("progressList", progressList);
        return "admin/my-progress";
    }

    /** Estadísticas */
    @GetMapping("/my-statistics")
    public String getMyStatistics(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();
        int currentYear = LocalDate.now().getYear();

        List<MonthlyStatisticsDTO> statsForYear = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {

                    int routines = monthlyStatisticsService.getUserRoutinesStarted(user.getUsername(), currentYear, month);
                    int recs = monthlyStatisticsService.getUserRecommendationsReceived(user.getUsername(), currentYear, month);
                    MonthlyStatisticsDTO dto = new MonthlyStatisticsDTO();
                    dto.setYear(currentYear);
                    dto.setMonth(month);
                    dto.setRoutinesStarted(routines);
                    dto.setRecommendationsReceived(recs);
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("statisticsData", statsForYear);
        model.addAttribute("currentYear", currentYear);
        return "admin/my-statistics";
    }

}