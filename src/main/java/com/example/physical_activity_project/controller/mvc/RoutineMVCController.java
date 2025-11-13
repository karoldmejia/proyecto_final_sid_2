package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.mappers.RoutineMapper;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.security.CustomUserDetails;
import com.example.physical_activity_project.services.IRoutineService;
import com.example.physical_activity_project.services.IUserService;
import com.example.physical_activity_project.services.IExerciseService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Controller
@RequestMapping("/user/routines")
@RequiredArgsConstructor
public class RoutineMVCController {

    private final IRoutineService routineService;
    private final RoutineMapper routineMapper;
    private final IUserService userService;
    private final IExerciseService exerciseService;

    // =====================================================
    // LISTAR TODAS LAS RUTINAS DEL USUARIO ACTUAL
    // =====================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('User', 'Trainer', 'Admin')")
    public String getAllRoutines(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUser().getUsername();

        // 🔹 Traer solo las rutinas del usuario logueado
        model.addAttribute("routines", routineService.getRoutinesByUser(username));

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/routine-list";
    }

    // =====================================================
    // FORMULARIO PARA AGREGAR RUTINA (ENTRENADOR O ADMIN)
    // =====================================================
    @GetMapping("/add")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String showAddRoutineForm(Model model, Authentication authentication) {
        model.addAttribute("routine", new RoutineDTO());
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/add";
    }

    // =====================================================
    // GUARDAR NUEVA RUTINA (ENTRENADOR O ADMIN)
    // =====================================================
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String addRoutine(@ModelAttribute("routine") RoutineDTO routineDTO, Authentication authentication) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);

        // ✅ Asociar correctamente el usuario que crea la rutina
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User creator = userDetails.getUser();
        routine.setUserSqlId(creator.getUsername());

        routineService.createRoutine(routine);
        return "redirect:/user/routines";
    }

    // =====================================================
    // EDITAR RUTINA EXISTENTE
    // =====================================================
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String showEditRoutineForm(@PathVariable ObjectId id, Model model, Authentication authentication) {
        Routine routine = routineService.getRoutineById(id);
        model.addAttribute("routine", routineMapper.entityToDto(routine));

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/edit";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String editRoutine(@PathVariable ObjectId id, @ModelAttribute("routine") RoutineDTO routineDTO, Authentication authentication) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);

        // ✅ Mantener asociación con el usuario que edita
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User editor = userDetails.getUser();
        routine.setUserSqlId(editor.getUsername());

        routineService.updateRoutine(id, routine);
        return "redirect:/user/routines";
    }

    // =====================================================
    // ELIMINAR RUTINA
    // =====================================================
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String deleteRoutine(@PathVariable ObjectId id) {
        routineService.deleteRoutine(id);
        return "redirect:/user/routines";
    }

    // =====================================================
    // FORMULARIO PARA CREAR RUTINA COMO USUARIO NORMAL
    // =====================================================
    @GetMapping("/my/new")
    @PreAuthorize("hasRole('User')")
    public String showUserCreateRoutineForm(Model model, Authentication authentication) {
        model.addAttribute("routine", new RoutineDTO());
        model.addAttribute("allExercises", exerciseService.getAllExercises());
        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/add";
    }

    // =====================================================
    // GUARDAR LA NUEVA RUTINA CREADA POR EL USUARIO NORMAL
    // =====================================================
    @PostMapping("/my/new")
    @PreAuthorize("hasRole('User')")
    public String createUserRoutine(@ModelAttribute("routine") RoutineDTO routineDTO,
                                    Authentication authentication) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        routine.setUserSqlId(currentUser.getUsername());

        routineService.createRoutine(routine);
        return "redirect:/user/routines/my";
    }

    // =====================================================
    // LISTAR LAS RUTINAS DEL USUARIO LOGUEADO
    // =====================================================
    @GetMapping("/my")
    @PreAuthorize("hasRole('User')")
    public String getUserRoutines(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUser().getUsername();

        // 🔹 Obtener solo las rutinas del usuario actual
        model.addAttribute("routines", routineService.getRoutinesByUser(username));

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/routine-list";
    }

    // =====================================================
    // SELECCIONAR LAYOUT SEGÚN ROL
    // =====================================================
    private String getLayout(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_Admin"))) {
            return "layouts/baseAdmin";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_Trainer"))) {
            return "layouts/baseTrainer";
        }
        return "layouts/baseUser";
    }
}
