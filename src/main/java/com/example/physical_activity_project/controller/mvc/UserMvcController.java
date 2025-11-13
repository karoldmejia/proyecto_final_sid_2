package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        // Aquí podrías agregar datos reales si quieres mostrar estadísticas del usuario:
        model.addAttribute("username", username);
        model.addAttribute("myActiveRoutines", 3);
        model.addAttribute("myMonthlyProgress", 5);
        model.addAttribute("trainerName", "Entrenador Ejemplo");

        return "user/dashboard";
    }

    /** Biblioteca de ejercicios */
    @GetMapping("/exercises")
    public String getExerciseLibrary(Model model) {
        model.addAttribute("exercises", exerciseService.getAllExercises());
        return "user/exercises/exercises-list";
    }

    /** Rutinas prediseñadas */
    @GetMapping("/routines/predefined")
    public String explorePredefinedRoutines(Model model) {
        // model.addAttribute("routines", routineService.getPredefinedRoutines());
        return "user/predefined-routines";
    }

    /** Mis rutinas */
    @GetMapping("/routines/my")
    public String getMyRoutines(Model model, Authentication authentication) {
        // model.addAttribute("myRoutines", routineService.getRoutinesForUser(authentication.getName()));
        return "user/my-routines";
    }

    /** Formulario para registrar progreso */
    @GetMapping("/log-activity")
    public String showLogProgressForm(Model model) {
        model.addAttribute("progressDTO", new ExerciseProgressDTO());
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        return "user/log-progress";
    }

    /** Guarda el registro de progreso */
    @PostMapping("/log-activity")
    public String saveProgress(@ModelAttribute("progressDTO") ExerciseProgressDTO dto,
                               Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();
        ExerciseProgress entity = exerciseProgressMapper.dtoToEntity(dto);
        progressService.registerProgress(user.getUsername(), entity);
        return "redirect:/user/my-progress";
    }

    /** Ver historial de progreso */
    @GetMapping("/my-progress")
    public String showMyProgress(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username).orElseThrow();

        List<ExerciseProgressDTO> progressList = progressService.getProgressByUser(user.getUsername())
                .stream()
                .map(exerciseProgressMapper::entityToDto)
                .toList();

        model.addAttribute("progressList", progressList);
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
}
