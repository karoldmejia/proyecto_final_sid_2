package com.example.physical_activity_project.controller.mvc;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.mappers.ExerciseMapper;
import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.services.IExerciseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mvc/exercises")
public class ExerciseViewController {

    @Autowired
    private IExerciseService exerciseService;

    @Autowired
    private ExerciseMapper mapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ObjectId.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    try {
                        setValue(new ObjectId(text));
                    } catch (IllegalArgumentException e) {
                        setValue(null);
                    }
                }
            }

            @Override
            public String getAsText() {
                ObjectId value = (ObjectId) getValue();
                return (value != null) ? value.toString() : "";
            }
        });
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_EJERCICIOS')")
    public String showExerciseListPage(Model model) {
        List<ExerciseDTO> list = exerciseService.getAllExercises()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        model.addAttribute("exercises", list);
        return "exercises/exercises-list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('CREAR_EJERCICIO')")
    public String showCreateExerciseForm(Model model) {
        model.addAttribute("exercise", new ExerciseDTO());
        model.addAttribute("pageTitle", "Crear Nuevo Ejercicio");
        return "exercises/exercise-form";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('EDITAR_EJERCICIO')")
    public String showEditExerciseForm(@PathVariable ObjectId id, Model model) {
        Exercise exercise = exerciseService.getExerciseById(id);
        model.addAttribute("exercise", mapper.entityToDto(exercise));
        model.addAttribute("pageTitle", "Editar Ejercicio");
        return "exercises/exercise-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('CREAR_EJERCICIO', 'EDITAR_EJERCICIO')")
    public String saveExercise(@ModelAttribute ExerciseDTO exerciseDTO,
                               BindingResult result,
                               Model model) {

        if (exerciseDTO.getName() == null || exerciseDTO.getName().trim().isEmpty()) {
            result.rejectValue("name", "NotBlank", "El nombre es requerido");
        }

        if (result.hasErrors()) {
            model.addAttribute("pageTitle", exerciseDTO.getId() != null ? "Editar Ejercicio" : "Crear Nuevo Ejercicio");
            return "redirect:/user/exercises";
        }

        try {
            Exercise exercise = mapper.dtoToEntity(exerciseDTO);
            exerciseService.createExercise(exercise);
            return "redirect:/mvc/exercises"; // ACTUALIZADO: redirección consistente
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar: " + e.getMessage());
            model.addAttribute("pageTitle", exerciseDTO.getId() != null ? "Editar Ejercicio" : "Crear Nuevo Ejercicio");
            return "redirect:/user/exercises";
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_EJERCICIO')")
    public String deleteExercise(@PathVariable ObjectId id) {
        exerciseService.deleteExercise(id);
        return "redirect:/user/exercises"; // ACTUALIZADO: redirección consistente
    }
}