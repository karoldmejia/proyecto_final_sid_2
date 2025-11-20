package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.mappers.ExerciseMapper;
import com.example.physical_activity_project.mappers.RoutineMapper;
import com.example.physical_activity_project.model.*;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.*;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc/trainer")
@AllArgsConstructor
public class TrainerController {

    private final IUserTrainerAssignmentService userTrainerAssignmentService;
    private final IRoutineService routineService;
    private final IUserService userService;
    private final IExerciseProgressService exerciseProgressService;
    private final IExerciseService exerciseService;
    private final ExerciseMapper exerciseMapper;
    private final RoutineMapper routineMapper;

    // =====================================================
    // DASHBOARD
    // =====================================================
    @GetMapping("/dashboard")
    public String trainerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentTrainer = userDetails.getUser();

        List<User> assignedUsers = userTrainerAssignmentService
                .getAssignmentsByTrainer(currentTrainer.getUsername())
                .stream().map(UserTrainerAssignment::getUser)
                .collect(Collectors.toList());

        model.addAttribute("assignedUsers", assignedUsers);
        model.addAttribute("trainerName", currentTrainer.getUsername());
        return "trainer/dashboard";
    }

    @GetMapping("/students")
    public String listAssignedStudents(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentTrainer = userDetails.getUser();

        List<User> assignedUsers = userTrainerAssignmentService
                .getAssignmentsByTrainer(currentTrainer.getUsername())
                .stream().map(UserTrainerAssignment::getUser)
                .collect(Collectors.toList());

        model.addAttribute("assignedUsers", assignedUsers);
        model.addAttribute("trainerName", currentTrainer.getUsername());
        return "trainer/student_list";
    }

    // =====================================================
    // RUTINAS DE UN USUARIO
    // =====================================================
    @GetMapping("/user/{userId}/routines")
    public String viewUserRoutines(@PathVariable String userId, Model model) {
        User user = userService.findByUsername(userId).orElse(null);
        if (user == null) return "redirect:/mvc/trainer/dashboard?error=UserNotFound";

        model.addAttribute("user", user);
        model.addAttribute("routines", routineService.getRoutinesByUser(userId));
        return "trainer/user_routines";
    }

    // =====================================================
    // PROGRESO DE USUARIO
    // =====================================================
    @GetMapping("/user/{userId}/progress")
    public String viewUserProgress(@PathVariable String userId, Model model) {
        User user = userService.findByUsername(userId).orElse(null);
        if (user == null) return "redirect:/mvc/trainer/dashboard?error=UserNotFound";

        model.addAttribute("user", user);
        model.addAttribute("progressList", exerciseProgressService.getProgressByUser(userId));
        return "trainer/user_progress";
    }

    @PostMapping("/progress/{progressId}/recommend")
    public String addRecommendation(@PathVariable String progressId,
                                    @RequestParam String content,
                                    @AuthenticationPrincipal CustomUserDetails trainer) {

        ExerciseProgress progress =
                exerciseProgressService.addRecommendation(new ObjectId(progressId),
                        trainer.getUser().getUsername(),
                        content);

        return "redirect:/mvc/trainer/user/" + progress.getUserId() + "/progress";
    }

    // =====================================================
    // CRUD EJERCICIOS
    // =====================================================
    @GetMapping("/exercises")
    public String listExercises(Model model) {
        model.addAttribute("exercises",
                exerciseService.getAllExercises().stream()
                        .map(exerciseMapper::entityToDto)
                        .collect(Collectors.toList())
        );
        return "trainer/exercise_list";
    }

    @GetMapping("/exercises/add")
    public String addExerciseForm(Model model) {
        model.addAttribute("exercise", new ExerciseDTO());
        return "trainer/exercise_form";
    }

    @PostMapping("/exercises")
    public String saveExercise(@ModelAttribute ExerciseDTO dto) {
        exerciseService.createExercise(exerciseMapper.dtoToEntity(dto));
        return "redirect:/mvc/trainer/exercises";
    }

    @GetMapping("/exercises/edit/{id}")
    public String editExerciseForm(@PathVariable ObjectId id, Model model) {
        Exercise exercise = exerciseService.getExerciseById(id);
        model.addAttribute("exercise", exerciseMapper.entityToDto(exercise));
        return "trainer/exercise_form";
    }

    @PostMapping("/exercises/update/{id}")
    public String updateExercise(@PathVariable ObjectId id,
                                 @ModelAttribute ExerciseDTO dto) {
        Exercise exercise = exerciseMapper.dtoToEntity(dto);
        exercise.setId(id);
        exerciseService.updateExercise(id, exercise);
        return "redirect:/mvc/trainer/exercises";
    }

    @PostMapping("/exercises/delete/{id}")
    public String deleteExercise(@PathVariable ObjectId id) {
        exerciseService.deleteExercise(id);
        return "redirect:/mvc/trainer/exercises";
    }

    // =====================================================
    // CRUD RUTINAS
    // =====================================================
    @GetMapping("/routines")
    public String listRoutines(Model model) {
        model.addAttribute("routines", routineService.getAllRoutines());
        return "trainer/routine_list";
    }

    @GetMapping("/routines/add")
    public String addRoutineForm(Model model) {
        model.addAttribute("routine", new RoutineDTO());
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        return "trainer/routine_form";
    }

    @PostMapping("/routines")
    public String saveRoutine(@ModelAttribute RoutineDTO dto,
                              @AuthenticationPrincipal CustomUserDetails trainer) {

        Routine routine = routineMapper.dtoToEntity(dto);
        routine.setUserSqlId(trainer.getUser().getUsername());

        routineService.createRoutine(routine);
        return "redirect:/mvc/trainer/routines";
    }

    @GetMapping("/routines/edit/{id}")
    public String editRoutineForm(@PathVariable String id, Model model) {

        ObjectId objectId = new ObjectId(id);
        Routine routine = routineService.getRoutineById(objectId);

        model.addAttribute("routine", routineMapper.entityToDto(routine));
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        return "trainer/routine_form";
    }

    @PostMapping("/routines/update/{id}")
    public String updateRoutine(@PathVariable String id,
                                @ModelAttribute RoutineDTO dto) {

        ObjectId objectId = new ObjectId(id);
        Routine updated = routineMapper.dtoToEntity(dto);

        Routine original = routineService.getRoutineById(objectId);

        updated.setId(objectId);
        updated.setUserSqlId(original.getUserSqlId());
        updated.setCreationDate(original.getCreationDate());
        updated.setOriginPublicId(original.getOriginPublicId());

        routineService.updateRoutine(objectId, updated);
        return "redirect:/mvc/trainer/routines";
    }

    @PostMapping("/routines/delete/{id}")
    public String deleteRoutine(@PathVariable String id) {
        routineService.deleteRoutine(new ObjectId(id));
        return "redirect:/mvc/trainer/routines";
    }

}
