package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.UserTrainerAssignmentDTO;
import com.example.physical_activity_project.mappers.UserTrainerAssignmentMapper;
import com.example.physical_activity_project.model.UserTrainerAssignment;
import com.example.physical_activity_project.services.impl.UserTrainerAssignmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/assignments")
@RequiredArgsConstructor
public class UserTrainerAssignmentController {

    private final UserTrainerAssignmentServiceImpl assignmentService;
    private final UserTrainerAssignmentMapper mapper;

    @PostMapping("/trainer/{trainerId}/user/{userId}")
    @PreAuthorize("hasAuthority('CREAR_ASIGNACION')")
    public ResponseEntity<UserTrainerAssignmentDTO> assignTrainer(
            @PathVariable String trainerId,
            @PathVariable String userId) {

        UserTrainerAssignment saved = assignmentService.assignTrainerToUser(trainerId, userId);
        return ResponseEntity.ok(mapper.entityToDto(saved));
    }

    @PutMapping("/{assignmentId}/status")
    @PreAuthorize("hasAuthority('ACTUALIZAR_ESTADO_ASIGNACION')")
    public ResponseEntity<UserTrainerAssignmentDTO> updateStatus(
            @PathVariable Long assignmentId,
            @RequestParam String newStatus) {

        UserTrainerAssignment updated = assignmentService.updateAssignmentStatus(assignmentId, newStatus);
        return ResponseEntity.ok(mapper.entityToDto(updated));
    }

    @GetMapping("/trainer/{trainerId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public ResponseEntity<List<UserTrainerAssignmentDTO>> getByTrainer(@PathVariable String trainerId) {
        List<UserTrainerAssignmentDTO> list = assignmentService.getAssignmentsByTrainer(trainerId)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('VER_ASIGNACIONES_PROPIAS')")
    public ResponseEntity<List<UserTrainerAssignmentDTO>> getByUser(@PathVariable String userId) {
        List<UserTrainerAssignmentDTO> list = assignmentService.getAssignmentsByUser(userId)
                .stream()
                .map(mapper::entityToDto)
                .toList();
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{assignmentId}")
    @PreAuthorize("hasAuthority('ELIMINAR_ASIGNACION')")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long assignmentId) {
        assignmentService.deleteAssignment(assignmentId);
        return ResponseEntity.noContent().build();
    }
}
