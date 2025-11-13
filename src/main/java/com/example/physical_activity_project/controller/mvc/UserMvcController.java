package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.mappers.MonthlyStatisticsMapper;
import com.example.physical_activity_project.model.*;
import com.example.physical_activity_project.services.*;
import com.example.physical_activity_project.dto.RecommendationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserMvcController {

    private final IExerciseService exerciseService;
    private final IRoutineService routineService;
    private final IExerciseProgressService progressService;
    private final IUserService userService;
    private final IMonthlyStatisticsService monthlyStatisticsService;
    private final ExerciseProgressMapper exerciseProgressMapper;

    /** Muestra el panel principal del usuario */
    @GetMapping("/dashboard")
    public String getUserDashboard(Model model, Authentication authentication) {
        String username = authentication.getName();

        int activeRoutinesCount = routineService.getRoutinesByUser(username).size();

        // Mantener los datos de ejemplo de main pero con datos reales donde estén disponibles
        model.addAttribute("username", username);
        model.addAttribute("myActiveRoutines", activeRoutinesCount);
        model.addAttribute("myMonthlyProgress", 5); // Mantener de main
        model.addAttribute("trainerName", "Entrenador Ejemplo"); // Mantener de main

        return "user/dashboard";
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
        progressService.registerProgress(user.getUsername(), entity);

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

        // 2. Obtener las estadísticas mensuales del usuario
        List<MonthlyStatisticsDTO> monthlyStats = monthlyStatisticsService.getMonthlyStatisticsForUser(user.getUsername());
        model.addAttribute("monthlyStats", monthlyStats);

        return "user/my-progress";
    }

    /** Estadísticas mensuales */
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

        return "user/my-statistics";
    }

    // Métodos adicionales de develop que no existían en main
    @GetMapping("/muscle-groups")
    public String showMuscleGroups(Model model) {
        // Asumiendo que IExerciseService puede devolver tipos de ejercicios distintos
        List<String> muscleGroups = exerciseService.getAllExercises().stream()
                .map(Exercise::getType)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("muscleGroups", muscleGroups);
        return "user/muscle-groups";
    }

    @GetMapping("/trainer-feedback")
    public String showTrainerFeedback(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();

        // Obtener todo el progreso de ejercicios del usuario
        List<ExerciseProgressDTO> allProgress = progressService.getProgressByUser(user.getUsername())
                .stream()
                .map(exerciseProgressMapper::entityToDto)
                .collect(Collectors.toList());

        // Recopilar todas las recomendaciones de todos los progresos, manejando valores nulos
        List<RecommendationDTO> trainerFeedback = allProgress.stream()
                .filter(progress -> progress.getRecommendations() != null) // Filtrar progresos sin recomendaciones
                .flatMap(progress -> progress.getRecommendations().stream())
                .collect(Collectors.toList());

        model.addAttribute("trainerFeedback", trainerFeedback);

        return "user/trainer-feedback";
    }
}