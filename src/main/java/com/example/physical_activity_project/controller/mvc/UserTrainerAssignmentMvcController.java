package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.services.impl.UserServiceImpl;
import com.example.physical_activity_project.services.impl.UserTrainerAssignmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/mvc/assignments")
@RequiredArgsConstructor
public class UserTrainerAssignmentMvcController {

    private final UserTrainerAssignmentServiceImpl assignmentService;
    private final UserServiceImpl userService;

    /**
     * Muestra la página de lista de asignaciones para un entrenador específico.
     */
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public String getByTrainer(@PathVariable Long trainerId, Model model) {
        // ... (sin cambios)
        List<UserTrainerAssignment> assignments = assignmentService.getAssignmentsByTrainer(trainerId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("contextTrainerId", trainerId);
        model.addAttribute("contextName", userService.getUserById(trainerId).orElse(new User()).getUsername());
        model.addAttribute("viewType", "trainer");
        return "assignments/list";
    }

    /**
     * Muestra la página de lista de asignaciones para un usuario específico.
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public String getByUser(@PathVariable Long userId, Model model) {
        // ... (sin cambios)
        List<UserTrainerAssignment> assignments = assignmentService.getAssignmentsByUser(userId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("contextUserId", userId);
        model.addAttribute("contextName", userService.getUserById(userId).orElse(new User()).getUsername());
        model.addAttribute("viewType", "user");
        return "assignments/list";
    }

    /**
     * Muestra el formulario para crear una nueva asignación.
     * Muestra un formulario para asignar un entrenador a un usuario específico.
     */
    @GetMapping("/create/user/{userId}")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public String showAssignFormForUser(@PathVariable Long userId, Model model) {

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));
        List<User> trainers = userService.getUsersByRoleName("Trainer");

        model.addAttribute("user", user);
        model.addAttribute("trainers", trainers);
        model.addAttribute("assignmentForm", new AssignmentForm(userId, null));
        model.addAttribute("viewMode", "assignTrainer");

        // ===== ¡CAMBIO AQUÍ! =====
        return "assignments/assign-form :: content";
    }

    /**
     * Muestra el formulario para asignar usuarios a un entrenador específico.
     */
    @GetMapping("/create/trainer/{trainerId}")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public String showAssignFormForTrainer(@PathVariable Long trainerId, Model model) {

        User trainer = userService.getUserById(trainerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trainer Id:" + trainerId));
        List<User> users = userService.getUsersByRoleName("User");

        model.addAttribute("trainer", trainer);
        model.addAttribute("users", users);
        model.addAttribute("assignmentForm", new AssignmentForm(null, trainerId));
        model.addAttribute("viewMode", "assignUser");


        return "assignments/assign-form :: content";
    }

    /**
     * Procesa el formulario de asignación de entrenador.
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public String assignTrainer(@ModelAttribute AssignmentForm assignmentForm,
                                RedirectAttributes redirectAttributes) {
        // ... (sin cambios)
        try {
            assignmentService.assignTrainerToUser(
                    assignmentForm.getTrainerId(),
                    assignmentForm.getUserId()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Asignación creada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al asignar: " + e.getMessage());
        }

        if (assignmentForm.getUserId() != null) {
            return "redirect:/mvc/assignments/user/" + assignmentForm.getUserId();
        } else {
            return "redirect:/mvc/assignments/trainer/" + assignmentForm.getTrainerId();
        }
    }

    /**
     * Procesa la actualización de estado.
     */
    @PostMapping("/update-status")
    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_ASIGNACION')")
    public String updateStatus(@RequestParam Long assignmentId,
                               @RequestParam String newStatus,
                               @RequestParam String redirectUrl,
                               RedirectAttributes redirectAttributes) {

        try {
            assignmentService.updateAssignmentStatus(assignmentId, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Estado actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar: " + e.getMessage());
        }
        return "redirect:" + redirectUrl;

    }

    /**
     * Procesa la eliminación de una asignación.
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ELIMINAR_ASIGNACION')")
    public String deleteAssignment(@RequestParam Long assignmentId,
                                   @RequestParam String redirectUrl,
                                   RedirectAttributes redirectAttributes) {
        // ... (sin cambios)
        try {
            assignmentService.deleteAssignment(assignmentId);
            redirectAttributes.addFlashAttribute("successMessage", "Asignación eliminada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar: " + e.getMessage());
        }
        return "redirect:" + redirectUrl;
    }

    /**
     * Clase DTO simple para el data-binding del formulario de asignación.
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    static class AssignmentForm {
        private Long userId;
        private Long trainerId;
    }
}