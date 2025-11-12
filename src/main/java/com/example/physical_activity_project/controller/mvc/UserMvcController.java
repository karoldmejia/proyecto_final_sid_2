package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.mappers.MonthlyStatisticsMapper;
import com.example.physical_activity_project.model.MonthlyStatistics;
import com.example.physical_activity_project.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('User')")
public class UserMvcController {

    // Inyecta los servicios que el usuario necesita
    private final IExerciseService exerciseService;
    private final IRoutineService routineService;
    private final IExerciseProgressService progressService;
    private final IUserService userService;
    private final ExerciseProgressMapper exerciseProgressMapper;

    private final IMonthlyStatisticsService monthlyStatisticsService;


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

    @GetMapping("/my-statistics")
    public String getMyStatistics(Model model, Authentication authentication) {
        // 1. Obtener el ID del usuario logueado
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();

        // 2. Obtener el año actual
        int currentYear = LocalDate.now().getYear();

        // 3. Obtenemos los 12 meses para la gráfica
        List<MonthlyStatisticsDTO> statsForYear = IntStream.rangeClosed(1, 12)
                .mapToObj(month -> {

                    // 4. Llama a cada método de tu servicio que devuelve un int
                    int routines = monthlyStatisticsService.getUserRoutinesStarted(user.getId(), currentYear, month);

                    // 5. ¡OJO! Tu interfaz no tiene "progressLoggedCount".
                    //    Uso el método que sí tienes: getUserRecommendationsReceived
                    int recs = monthlyStatisticsService.getUserRecommendationsReceived(user.getId(), currentYear, month);

                    // 6. Construye el DTO manualmente (sin mapper)
                    MonthlyStatisticsDTO dto = new MonthlyStatisticsDTO();
                    dto.setYear(currentYear);
                    dto.setMonth(month);
                    dto.setRoutinesStarted(routines);

                    // 7. ASEGÚRATE de que tu DTO tenga este campo (ver nota abajo)
                    dto.setRecommendationsReceived(recs);

                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("statisticsData", statsForYear);
        model.addAttribute("currentYear", currentYear);

        return "user/my-statistics"; // La nueva plantilla
    }

}

