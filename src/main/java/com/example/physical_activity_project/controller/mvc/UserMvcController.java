package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.services.IExerciseService;
// Importa tus otros servicios cuando los tengas
// import com.example.physical_activity_project.services.IRoutineService;
// import com.example.physical_activity_project.services.IProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user") // <-- Esta es la clave para tus rutas
@RequiredArgsConstructor
@PreAuthorize("hasRole('User')") // Asegura que solo el rol 'User' entre aquí
public class UserMvcController {

    // Inyecta los servicios que el usuario necesita
    private final IExerciseService exerciseService;
    // private final IRoutineService routineService;
    // private final IProgressService progressService;

    /**
     * Muestra el panel principal del usuario.
     * Ruta: /user/dashboard
     */
    @GetMapping("/dashboard")
    public String getUserDashboard(Model model, Authentication authentication) {
        // Obtienes el nombre del usuario logueado
        String username = authentication.getName();

        // Aquí cargas la información personal del usuario
        // ej: model.addAttribute("myRoutines", routineService.getRoutinesForUser(username));
        // ej: model.addAttribute("myProgress", progressService.getProgressForUser(username)); [cite: 11]

        model.addAttribute("username", username);

        // Devuelve la plantilla Thymeleaf del dashboard de usuario
        return "user/dashboard";
    }

    /**
     * Muestra la biblioteca de ejercicios que el usuario puede consultar.
     * Ruta: /user/exercises
     */
    @GetMapping("/exercises")
    public String getExerciseLibrary(Model model) {
        // El usuario puede ver todos los ejercicios predefinidos [cite: 9]
        model.addAttribute("exercises", exerciseService.getAllExercises());

        // Devuelve la plantilla de la biblioteca de ejercicios
        return "user/exercises/exercises-list";
    }

    /**
     * Muestra las rutinas prediseñadas por los entrenadores.
     * Ruta: /user/routines/explore
     */
    @GetMapping("/routines/explore")
    public String explorePredefinedRoutines(Model model) {
        // El usuario consulta rutinas prediseñadas [cite: 13]
        // model.addAttribute("routines", routineService.getPredefinedRoutines());

        // Devuelve la plantilla para explorar rutinas
        return "user/predefined-routines";
    }

    /**
     * Muestra las rutinas que el usuario ha adoptado o creado.
     * Ruta: /user/routines/my
     */
    @GetMapping("/routines/my")
    public String getMyRoutines(Model model, Authentication authentication) {
        // Carga solo las rutinas de este usuario [cite: 8, 14]
        // model.addAttribute("myRoutines", routineService.getRoutinesForUser(authentication.getName()));

        // Devuelve la plantilla para ver "Mis Rutinas"
        return "user/my-routines";
    }

    // Aquí puedes añadir más métodos, como:
    // @GetMapping("/progress/log") para mostrar el formulario de registrar progreso [cite: 11]
    // @PostMapping("/progress/log") para guardar el progreso [cite: 11]
    // @PostMapping("/routines/adopt/{id}") para adoptar una rutina [cite: 14]
}