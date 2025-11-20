package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.IExerciseProgressService;
import com.example.physical_activity_project.services.IExerciseService;
import com.example.physical_activity_project.services.IRoutineService;
import com.example.physical_activity_project.services.IUserService;
import com.example.physical_activity_project.services.IUserTrainerAssignmentService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/dashboard")
    public String trainerDashboard(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentTrainer = userDetails.getUser();
        List<UserTrainerAssignment> assignments = userTrainerAssignmentService.getAssignmentsByTrainer(currentTrainer.getUsername());

        // Extraemos solo los usuarios de las asignaciones
        List<User> assignedUsers = assignments.stream()
                                              .map(UserTrainerAssignment::getUser)
                                              .collect(Collectors.toList());

        model.addAttribute("assignedUsers", assignedUsers);
        model.addAttribute("trainerName", currentTrainer.getUsername());
        return "trainer/dashboard";
    }

    @GetMapping("/user/{userId}/routines")
    public String viewUserRoutines(@PathVariable("userId") String userId, Model model) {
        User user = userService.findByUsername(userId).orElse(null);
        if (user == null) {
            return "redirect:/mvc/trainer/dashboard?error=UserNotFound";
        }

        List<Routine> routines = routineService.getRoutinesByUser(userId);
        model.addAttribute("user", user);
        model.addAttribute("routines", routines);
        return "trainer/user_routines";
    }

    @GetMapping("/user/{userId}/progress")
    public String viewUserProgress(@PathVariable("userId") String userId, Model model) {
        User user = userService.findByUsername(userId).orElse(null);
        if (user == null) {
            return "redirect:/mvc/trainer/dashboard?error=UserNotFound";
        }

        List<ExerciseProgress> progressList = exerciseProgressService.getProgressByUser(userId);
        model.addAttribute("user", user);
        model.addAttribute("progressList", progressList);
        return "trainer/user_progress";
    }

    @PostMapping("/progress/{progressId}/recommend")
    public String addRecommendation(@PathVariable("progressId") String progressId,
                                    @RequestParam("content") String content,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        User currentTrainer = userDetails.getUser();
        ExerciseProgress progress = exerciseProgressService.addRecommendation(new org.bson.types.ObjectId(progressId), currentTrainer.getUsername(), content);
        String userId = progress.getUserId();
        return "redirect:/mvc/trainer/user/" + userId + "/progress";
    }

    @GetMapping("/exercises")
    public String listExercises(Model model) {
        List<Exercise> exercises = exerciseService.getAllExercises();
        model.addAttribute("exercises", exercises);
        return "trainer/exercise_list";
    }

    @GetMapping("/exercises/add")
    public String addExerciseForm(Model model) {
        model.addAttribute("exercise", new Exercise());
        return "trainer/exercise_form";
    }

    @PostMapping("/exercises")
    public String saveExercise(@ModelAttribute Exercise exercise) {
        exerciseService.createExercise(exercise);
        return "redirect:/mvc/trainer/exercises";
    }

    @GetMapping("/routines")
    public String listRoutines(Model model) {
        List<Routine> routines = routineService.getAllRoutines();
        model.addAttribute("routines", routines);
        return "trainer/routine_list";
    }

    @GetMapping("/routines/add")
    public String addRoutineForm(Model model) {
        model.addAttribute("routine", new Routine());
        return "trainer/routine_form";
    }

    @PostMapping("/routines")
    public String saveRoutine(@ModelAttribute Routine routine) {
        // The 'certified' property is bound from the form
        routineService.createRoutine(routine);
        return "redirect:/mvc/trainer/routines";
    }
}
