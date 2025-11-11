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
    // Necesitaremos servicios de User para poblar formularios
    private final UserServiceImpl userService;

    /**
     * Muestra la página de lista de asignaciones para un entrenador específico.
     * (Equivalente a tu GET /trainer/{trainerId})
     */
    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public String getByTrainer(@PathVariable Long trainerId, Model model) {

        List<UserTrainerAssignment> assignments = assignmentService.getAssignmentsByTrainer(trainerId);

        model.addAttribute("assignments", assignments);
        // Pasamos el ID del entrenador para que la vista sepa el contexto
        model.addAttribute("contextTrainerId", trainerId);
        // Pasamos el nombre del entrenador para el título de la página
        model.addAttribute("contextName", userService.getUserById(trainerId).orElse(new User()).getUsername());
        model.addAttribute("viewType", "trainer"); // Para el título

        return "assignments/list"; // -> Llama a resources/templates/assignments/list.html
    }

    /**
     * Muestra la página de lista de asignaciones para un usuario específico.
     * (Equivalente a tu GET /user/{userId})
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public String getByUser(@PathVariable Long userId, Model model) {

        List<UserTrainerAssignment> assignments = assignmentService.getAssignmentsByUser(userId);

        model.addAttribute("assignments", assignments);
        model.addAttribute("contextUserId", userId);
        model.addAttribute("contextName", userService.getUserById(userId).orElse(new User()).getUsername());
        model.addAttribute("viewType", "user"); // Para el título

        return "assignments/list"; // -> Llama a resources/templates/assignments/list.html
    }

    /**
     * Muestra el formulario para crear una nueva asignación.
     * Típicamente, se le pasaría el ID del usuario desde la página de "detalles de usuario".
     */
    @GetMapping("/new")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public String showAssignForm(@RequestParam("userId") Long userId, Model model) {

        // Buscamos al usuario para mostrar su nombre
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + userId));

        // Buscamos a todos los entrenadores para ponerlos en un <select>
        List<User> trainers = userService.getUsersByRoleName("TRAINER"); // Asumiendo que tienes un método así

        model.addAttribute("user", user);
        model.addAttribute("trainers", trainers);

        // Pasamos un objeto vacío para el data-binding del formulario
        model.addAttribute("assignmentForm", new AssignmentForm(userId, null));

        return "assignments/assign-form"; // -> Llama a resources/templates/assignments/assign-form.html
    }

    /**
     * Procesa el formulario de asignación de entrenador.
     * (Equivalente a tu POST /trainer/{trainerId}/user/{userId})
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public String assignTrainer(@ModelAttribute AssignmentForm assignmentForm, RedirectAttributes redirectAttributes) {

        try {
            assignmentService.assignTrainerToUser(
                    assignmentForm.getTrainerId(),
                    assignmentForm.getUserId()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Entrenador asignado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al asignar: " + e.getMessage());
        }

        // Redirigimos de vuelta a la página del usuario
        return "redirect:/mvc/users/edit/" + assignmentForm.getUserId(); // O a donde prefieras
    }

    /**
     * Procesa la actualización de estado.
     * Esto vendría de un pequeño formulario en la lista.
     * (Equivalente a tu PUT /{assignmentId}/status)
     */
    @PostMapping("/update-status")
    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_ASIGNACION')")
    public String updateStatus(@RequestParam Long assignmentId,
                               @RequestParam String newStatus,
                               @RequestParam String redirectUrl, // URL para saber a dónde volver
                               RedirectAttributes redirectAttributes) {
        try {
            assignmentService.updateAssignmentStatus(assignmentId, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Estado actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar: " + e.getMessage());
        }

        // Redirigimos de vuelta a la lista de donde venimos
        return "redirect:" + redirectUrl;
    }

    /**
     * Procesa la eliminación de una asignación.
     * (Equivalente a tu DELETE /{assignmentId})
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('ELIMINAR_ASIGNACION')")
    public String deleteAssignment(@RequestParam Long assignmentId,
                                   @RequestParam String redirectUrl,
                                   RedirectAttributes redirectAttributes) {
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
     * Puedes ponerla como una clase interna o en su propio archivo.
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    static class AssignmentForm {
        private Long userId;
        private Long trainerId;
    }
}