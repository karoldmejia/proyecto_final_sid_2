package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.mappers.ExerciseMapper;
import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.services.IExerciseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    @Autowired
    private IExerciseService exerciseService;

    @Autowired
    private ExerciseMapper mapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_EJERCICIO')")
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody ExerciseDTO dto) {
        Exercise entity = mapper.dtoToEntity(dto);
        Exercise saved = exerciseService.createExercise(entity);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_EJERCICIOS')")
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseDTO> list = exerciseService.getAllExercises()
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_EJERCICIOS')")
    public ResponseEntity<ExerciseDTO> getExerciseById(@PathVariable ObjectId id) {
        Exercise exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(mapper.entityToDto(exercise));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_EJERCICIO')")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable ObjectId id, @RequestBody ExerciseDTO dto) {
        Exercise updated = mapper.dtoToEntity(dto);
        Exercise saved = exerciseService.updateExercise(id, updated);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_EJERCICIO')")
    public ResponseEntity<String> deleteExercise(@PathVariable ObjectId id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.ok("Exercise deleted successfully.");
    }
}
