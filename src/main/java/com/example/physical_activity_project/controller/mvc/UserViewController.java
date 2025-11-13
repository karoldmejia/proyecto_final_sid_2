package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.mappers.ExerciseMapper;
import com.example.physical_activity_project.mappers.RoutineMapper;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.services.IRoutineService;
import com.example.physical_activity_project.services.IUserService;
import com.example.physical_activity_project.services.IExerciseService;
import com.example.physical_activity_project.services.IMonthlyStatisticsService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserViewController {

    @Autowired
    private IRoutineService routineService;

    @Autowired
    private RoutineMapper routineMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IExerciseService exerciseService;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private IMonthlyStatisticsService monthlyStatisticsService;

    @GetMapping("/predefined-routines")
    public String listPredefinedRoutines(Model model, Principal principal) {
        String username = principal.getName();
        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/login?error=userNotFound";
        }
        User user = optionalUser.get();

        List<RoutineDTO> allPredefinedRoutines = routineService.getPredefinedRoutines()
                .stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());

        // Obtener los IDs de las rutinas predefinidas que el usuario ya ha adoptado
        List<ObjectId> adoptedOriginIds = routineService.getAdoptedRoutineOriginIdsByUser(user.getUsername());

        // Filtrar las rutinas predefinidas para excluir las ya adoptadas
        List<RoutineDTO> predefinedRoutines = allPredefinedRoutines.stream()
                .filter(routine -> !adoptedOriginIds.contains(routine.getId()))
                .collect(Collectors.toList());

        model.addAttribute("predefinedRoutines", predefinedRoutines);
        return "user/predefined-routines";
    }

    public String adoptRoutine(@PathVariable String id, Principal principal) {
        String username = principal.getName();
        Optional<User> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return "redirect:/login?error=userNotFound";
        }
        User user = optionalUser.get();

        routineService.adoptRoutine(user.getUsername(), new ObjectId(id));

        return "redirect:/user/dashboard";
    }
}
