package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.dto.RecommendationDTO;
import com.example.physical_activity_project.dto.RecommendationRequest;
import com.example.physical_activity_project.mappers.ExerciseProgressMapper;
import com.example.physical_activity_project.mappers.RecommendationMapper;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.Recommendation;
import com.example.physical_activity_project.services.impl.ExerciseProgressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
public class ExerciseProgressController {

    private final ExerciseProgressServiceImpl progressService;
    private final ExerciseProgressMapper mapper;
    private final RecommendationMapper recommendationMapper;


    @GetMapping
    @PreAuthorize("hasAuthority('VER_TODO_PROGRESO')")
    public ResponseEntity<List<ExerciseProgressDTO>> getAll() {
        List<ExerciseProgressDTO> list = progressService.getAllProgress()
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_PROGRESO_PROPIO') or hasAuthority('VER_PROGRESO_USUARIOS_ASIGNADOS') or hasAuthority('VER_TODO_PROGRESO')")
    public ResponseEntity<List<ExerciseProgressDTO>> getByUser(@PathVariable Long userId) {
        List<ExerciseProgressDTO> list = progressService.getProgressByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('REGISTRAR_PROGRESO_PROPIO')")
    public ResponseEntity<ExerciseProgressDTO> registerProgress(
            @PathVariable Long userId,
            @RequestBody ExerciseProgressDTO dto) {
        ExerciseProgress entity = mapper.dtoToEntity(dto);
        ExerciseProgress saved = progressService.registerProgress(userId, entity);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @PutMapping("/{progressId}")
    @PreAuthorize("hasAuthority('EDITAR_PROGRESO_PROPIO')")
    public ResponseEntity<ExerciseProgressDTO> updateProgress(
            @PathVariable ObjectId progressId,
            @RequestBody ExerciseProgressDTO dto) {
        ExerciseProgress entity = mapper.dtoToEntity(dto);
        ExerciseProgress updated = progressService.updateProgress(progressId, entity);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @DeleteMapping("/{progressId}")
    @PreAuthorize("hasAuthority('ELIMINAR_PROGRESO_PROPIO')")
    public ResponseEntity<Void> deleteProgress(@PathVariable ObjectId progressId) {
        progressService.deleteProgress(progressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}/week")
    @PreAuthorize("hasAuthority('VER_PROGRESO_PROPIO') or hasAuthority('VER_PROGRESO_USUARIOS_ASIGNADOS') or hasAuthority('VER_TODO_PROGRESO')")
    public ResponseEntity<List<ExerciseProgressDTO>> getWeeklyProgress(
            @PathVariable Long userId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        List<ExerciseProgressDTO> list = progressService.getProgressByWeek(userId, startDate)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}/summary")
    @PreAuthorize("hasAuthority('VER_PROGRESO_PROPIO') or hasAuthority('VER_PROGRESO_USUARIOS_ASIGNADOS') or hasAuthority('VER_TODO_PROGRESO')")
    public ResponseEntity<ProgressDTO> getSummary(
            @PathVariable Long userId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        ProgressDTO summary = progressService.getProgressSummary(userId, start, end);
        return ResponseEntity.ok(summary);
    }

    // Recommendations endpoints

    @PostMapping("/{progressId}/trainers/{trainerId}/recommendations")
    @PreAuthorize("hasAuthority('CREAR_RECOMENDACION')")
    public ResponseEntity<ExerciseProgressDTO> addRecommendation(
            @PathVariable ObjectId progressId,
            @PathVariable Long trainerId,
            @RequestBody RecommendationRequest request) {

        ExerciseProgress updated = progressService.addRecommendation(progressId, trainerId, request.getContent());
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }


    @GetMapping("/{progressId}/recommendations")
    @PreAuthorize("hasAuthority('VER_RECOMENDACIONES_PROPIAS') or hasAuthority('VER_RECOMENDACIONES_CREADAS')")
    public ResponseEntity<List<RecommendationDTO>> getRecommendations(@PathVariable ObjectId progressId) {
        ExerciseProgress progress = progressService.getProgressById(progressId);
        List<RecommendationDTO> list = progress.getRecommendations()
                .stream()
                .map(recommendationMapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{progressId}/recommendations/{index}/status")
    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_RECOMENDACION')")
    public ResponseEntity<ExerciseProgressDTO> updateRecommendationStatus(
            @PathVariable ObjectId progressId,
            @PathVariable int index,
            @RequestParam String newStatus) {

        ExerciseProgress updatedProgress = progressService.updateRecommendationStatus(progressId, index, newStatus);
        return ResponseEntity.ok(mapper.entityToDto(updatedProgress));
    }

    @DeleteMapping("/{progressId}/recommendations/{index}")
    @PreAuthorize("hasAuthority('ELIMINAR_RECOMENDACION')")
    public ResponseEntity<Void> deleteRecommendation(
            @PathVariable ObjectId progressId,
            @PathVariable int index) {

        progressService.deleteRecommendation(progressId, index);
        return ResponseEntity.noContent().build();
    }
}
