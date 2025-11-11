package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.mappers.RoutineExerciseMapper;
import com.example.physical_activity_project.mappers.RoutineMapper;
import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.services.IRoutineService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    @Autowired
    private IRoutineService routineService;

    @Autowired
    private RoutineMapper routineMapper;

    @Autowired
    private RoutineExerciseMapper routineExerciseMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREAR_RUTINA') or hasAuthority('CREAR_RUTINA_CERTIFICADA')")
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody RoutineDTO routineDTO) {
        Routine routine = routineMapper.dtoToEntity(routineDTO);
        Routine saved = routineService.createRoutine(routine);
        return ResponseEntity.ok(routineMapper.entityToDto(saved));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        List<RoutineDTO> routines = routineService.getAllRoutines()
                .stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VER_RUTINAS_PROPIAS') or hasAuthority('VER_TODAS_RUTINAS')")

    public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable ObjectId id) {
        Routine routine = routineService.getRoutineById(id);
        return ResponseEntity.ok(routineMapper.entityToDto(routine));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('EDITAR_RUTINA_PROPIA') or hasAuthority('EDITAR_CUALQUIER_RUTINA')")
    public ResponseEntity<RoutineDTO> updateRoutine(@PathVariable ObjectId id, @RequestBody RoutineDTO dto) {
        Routine updated = routineMapper.dtoToEntity(dto);
        Routine saved = routineService.updateRoutine(id, updated);
        return ResponseEntity.ok(routineMapper.entityToDto(saved));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ELIMINAR_RUTINA_PROPIA') or hasAuthority('ELIMINAR_CUALQUIER_RUTINA')")
    public ResponseEntity<String> deleteRoutine(@PathVariable ObjectId id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.ok("Routine deleted successfully.");
    }

    // Ejercicios embebidos

    @PostMapping("/{routineId}/exercises")
    public ResponseEntity<RoutineDTO> addExercise(@PathVariable ObjectId routineId,
                                                  @RequestBody RoutineExerciseDTO dto) {
        RoutineExercise exercise = routineExerciseMapper.dtoToEntity(dto);
        Routine updated = routineService.addExerciseToRoutine(routineId, exercise);
        return ResponseEntity.ok(routineMapper.entityToDto(updated));
    }

    @PutMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<RoutineDTO> updateExercise(@PathVariable ObjectId routineId,
                                                     @PathVariable ObjectId exerciseId,
                                                     @RequestBody RoutineExerciseDTO dto) {
        RoutineExercise updatedExercise = routineExerciseMapper.dtoToEntity(dto);
        Routine updated = routineService.updateExerciseInRoutine(routineId, exerciseId, updatedExercise);
        return ResponseEntity.ok(routineMapper.entityToDto(updated));
    }

    @DeleteMapping("/{routineId}/exercises/{exerciseId}")
    public ResponseEntity<RoutineDTO> removeExercise(@PathVariable ObjectId routineId,
                                                     @PathVariable ObjectId exerciseId) {
        Routine updated = routineService.removeExerciseFromRoutine(routineId, exerciseId);
        return ResponseEntity.ok(routineMapper.entityToDto(updated));
    }

}
