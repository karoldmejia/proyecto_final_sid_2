package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.services.IExerciseProgressService;
import com.example.physical_activity_project.services.IExerciseService;
// Importa tus otros servicios cuando los tengas
// import com.example.physical_activity_project.services.IRoutineService;
// import com.example.physical_activity_project.services.IProgressService;
import com.example.physical_activity_project.services.IRoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.IUserService;
import com.example.physical_activity_project.services.impl.ExerciseProgressServiceImpl;
import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.bson.types.ObjectId;

import java.util.List;

@Controller
@RequestMapping("/user") // <-- Esta es la clave para tus rutas
@RequiredArgsConstructor
@PreAuthorize("hasRole('User')") // Asegura que solo el rol 'User' entre aquí
public class UserMvcController {

    // Inyecta los servicios que el usuario necesita
    private final IExerciseService exerciseService;
    private final IRoutineService routineService;
    private final IExerciseProgressService progressService;
    private final IUserService userService;
    private final ExerciseProgressMapper exerciseProgressMapper;

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

    /**
     * Muestra el formulario para registrar un nuevo progreso.
     * Implementa: POST /api/progress/user/{userId}
     */
    @GetMapping("/log-progress")
    public String showLogProgressForm(Model model) {
        model.addAttribute("progressDTO", new ExerciseProgressDTO());
        // El formulario necesita la lista de ejercicios para un dropdown
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        return "user/log-progress";
    }

    /**
     * Guarda el nuevo registro de progreso.
     */
    @PostMapping("/log-progress")
    public String saveProgress(@ModelAttribute("progressDTO") ExerciseProgressDTO dto,
                               Authentication authentication) {

        // 1. Obtener el ID de usuario desde la sesión
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();

        // 2. Convertir DTO a Entidad
        ExerciseProgress entity = exerciseProgressMapper.dtoToEntity(dto);

        // 3. Guardar el progreso
        progressService.registerProgress(user.getId(), entity);

        return "redirect:/user/my-progress"; // Redirige a la lista de progreso
    }

    /**
     * Muestra la lista del progreso personal del usuario.
     * Implementa: GET /api/progress/user/{userId}
     * Implementa: GET /api/progress/{progressId}/recommendations
     */
    @GetMapping("/my-progress")
    public String showMyProgress(Model model, Authentication authentication) {
        // 1. Obtener el ID de usuario
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();


        // 2. Obtener su historial de progreso
        List<ExerciseProgressDTO> progressList = progressService.getProgressByUser(user.getId())
                .stream()
                .map(exerciseProgressMapper::entityToDto)
                .toList();


        model.addAttribute("progressList", progressList);
        return "user/my-progress";
    }
}

