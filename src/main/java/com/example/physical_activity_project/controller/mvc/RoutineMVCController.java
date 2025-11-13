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
import org.springframework.security.access.AccessDeniedException;
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
    // 1. LISTAR TODAS (Para Admins/Trainers)
    // =====================================================
    @GetMapping
    @PreAuthorize("hasAnyRole('User', 'Trainer', 'Admin')")
    public String getAllRoutines(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUser().getUsername();

        // Si es user normal, mostramos las suyas. Si es admin/trainer, la lógica podría variar
        // pero por ahora reutilizamos el servicio que filtra por usuario
        model.addAttribute("routines", routineService.getRoutinesByUser(username));

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        // IMPORTANTE: Apunta a la carpeta unificada
        return "routines/routine-list";
    }

    // =====================================================
    // 2. LISTAR "MIS RUTINAS" (ESTE ERA EL QUE TE FALTABA)
    // =====================================================
    @GetMapping("/my")
    @PreAuthorize("hasRole('User')")
    public String getUserRoutines(Model model, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUser().getUsername();

        model.addAttribute("routines", routineService.getRoutinesByUser(username));

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        // IMPORTANTE: Apunta a la carpeta unificada
        return "routines/routine-list";
    }

    // =====================================================
    // 3. CREAR RUTINA (Usuario Normal)
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

    @PostMapping("/my/new")
    @PreAuthorize("hasRole('User')")
    public String createUserRoutine(@ModelAttribute("routine") RoutineDTO routineDTO,
                                    Authentication authentication) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        routine.setUserSqlId(userDetails.getUser().getUsername());

        routineService.createRoutine(routine);
        return "redirect:/user/routines/my";
    }

    // =====================================================
    // 4. CREAR RUTINA (Trainer/Admin)
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

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin')")
    public String addRoutine(@ModelAttribute("routine") RoutineDTO routineDTO, Authentication authentication) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        routine.setUserSqlId(userDetails.getUser().getUsername());

        routineService.createRoutine(routine);
        return "redirect:/user/routines";
    }

    // =====================================================
    // 5. EDITAR RUTINA (GET)
    // =====================================================
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin', 'User')")
    public String showEditRoutineForm(@PathVariable String id, Model model, Authentication authentication) {
        ObjectId objectId = new ObjectId(id); // Conversión segura

        validateOwnership(objectId, authentication);

        Routine routine = routineService.getRoutineById(objectId);
        model.addAttribute("routine", routineMapper.entityToDto(routine));
        model.addAttribute("allExercises", exerciseService.getAllExercises());

        String layout = getLayout(authentication);
        model.addAttribute("layout", layout);
        return "routines/edit";
    }

    // =====================================================
    // 6. GUARDAR EDICIÓN (POST) - CORREGIDO
    // =====================================================
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin', 'User')")
    public String editRoutine(@PathVariable String id, @ModelAttribute("routine") RoutineDTO routineDTO, Authentication authentication) {
        ObjectId objectId = new ObjectId(id);

        validateOwnership(objectId, authentication);

        // 1. Recuperamos la rutina ORIGINAL de la base de datos
        Routine originalRoutine = routineService.getRoutineById(objectId);

        // 2. Convertimos los datos NUEVOS del formulario
        Routine routineToUpdate = routineMapper.dtoToEntity(routineDTO);

        // 3. RESTAURAMOS los datos que no venían en el formulario para no perderlos
        routineToUpdate.setId(objectId);
        routineToUpdate.setUserSqlId(originalRoutine.getUserSqlId()); // Mantenemos el dueño original
        routineToUpdate.setCreationDate(originalRoutine.getCreationDate()); // Restauramos la fecha original
        routineToUpdate.setOriginPublicId(originalRoutine.getOriginPublicId()); // Restauramos el ID público original
        routineService.updateRoutine(objectId, routineToUpdate);

        if (isUser(authentication)) {
            return "redirect:/user/routines/my";
        }
        return "redirect:/user/routines";
    }

    // =====================================================
    // 7. ELIMINAR (POST)
    // =====================================================
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('Trainer', 'Admin', 'User')")
    public String deleteRoutine(@PathVariable String id, Authentication authentication) {
        ObjectId objectId = new ObjectId(id); // Conversión segura

        validateOwnership(objectId, authentication);

        routineService.deleteRoutine(objectId);

        if (isUser(authentication)) {
            return "redirect:/user/routines/my";
        }
        return "redirect:/user/routines";
    }

    // =====================================================
    // MÉTODOS DE AYUDA
    // =====================================================
    private void validateOwnership(ObjectId routineId, Authentication authentication) {
        if (!isUser(authentication)) {
            return; // Admins/Trainers pueden editar todo
        }

        Routine routine = routineService.getRoutineById(routineId);
        String currentUsername = authentication.getName();

        if (!routine.getUserSqlId().equals(currentUsername)) {
            throw new AccessDeniedException("No tienes permiso para modificar esta rutina.");
        }
    }

    private boolean isUser(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_User"));
    }

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