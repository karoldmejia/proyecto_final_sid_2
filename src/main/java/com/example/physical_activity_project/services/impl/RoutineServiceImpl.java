package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.Routine;
import com.example.physical_activity_project.model.RoutineExercise;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.services.IRoutineService;
import com.example.physical_activity_project.services.IUserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutineServiceImpl implements IRoutineService {

    @Autowired
    private IRoutineRepository routineRepository;

    @Autowired
    private MonthlyStatisticsServiceImpl monthlyStatisticsService;

    @Autowired
    private IUserService userService;

    // --- Rutinas ---
    @Override
    public Routine createRoutine(Routine routine) {
        routine.setCreationDate(new Date());
        Routine saved = routineRepository.save(routine);

        // Actualizar estadística de rutinas iniciadas
        if (routine.getUserSqlId() != null) {
            monthlyStatisticsService.incrementRoutinesStarted(routine.getUserSqlId());
        }

        return saved;
    }

    @Override
    public Routine updateRoutine(ObjectId id, Routine updatedRoutine) {
        Routine existing = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine not found with id: " + id));

        existing.setName(updatedRoutine.getName());
        existing.setOriginPublicId(updatedRoutine.getOriginPublicId());
        existing.setUserSqlId(updatedRoutine.getUserSqlId());
        // reemplaza lista completa de ejercicios si se desea
        existing.setExercises(updatedRoutine.getExercises());

        return routineRepository.save(existing);
    }

    @Override
    public void deleteRoutine(ObjectId id) {
        if (routineRepository.existsById(id)) {
            routineRepository.deleteById(id);
        } else {
            throw new RuntimeException("Routine not found with id: " + id);
        }
    }

    @Override
    public List<Routine> getRoutinesByUser(String userSqlId) {
        return routineRepository.findByUserSqlId(userSqlId);
    }

    @Override
    public List<Routine> getAllRoutines() {
        return routineRepository.findAll();
    }

    @Override
    public Routine getRoutineById(ObjectId id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Routine not found with id: " + id));
    }


    // --- Ejercicios embebidos ---
    @Override
    public Routine addExerciseToRoutine(ObjectId routineId, RoutineExercise newExercise) {
        Routine routine = getRoutineById(routineId);
        routine.getExercises().add(newExercise);
        return routineRepository.save(routine);
    }

    @Override
    public Routine updateExerciseInRoutine(ObjectId routineId, ObjectId exerciseId, RoutineExercise updated) {
        Routine routine = getRoutineById(routineId);

        routine.getExercises().stream()
                .filter(e -> e.getExerciseId().equals(exerciseId))
                .findFirst()
                .ifPresent(e -> {
                    e.setNameSnapshot(updated.getNameSnapshot());
                    e.setSets(updated.getSets());
                    e.setReps(updated.getReps());
                    e.setTime(updated.getTime());
                });

        return routineRepository.save(routine);
    }

    @Override
    public Routine removeExerciseFromRoutine(ObjectId routineId, ObjectId exerciseId) {
        Routine routine = getRoutineById(routineId);
        routine.getExercises().removeIf(e -> e.getExerciseId().equals(exerciseId));
        return routineRepository.save(routine);
    }

    @Override
    public Routine adoptRoutine(String userId, ObjectId predefinedRoutineId) {
        Routine predefinedRoutine = routineRepository.findById(predefinedRoutineId)
                .orElseThrow(() -> new RuntimeException("Predefined routine not found with id: " + predefinedRoutineId));

        // Crear una nueva rutina basada en la predefinida
        Routine adoptedRoutine = new Routine();
        adoptedRoutine.setUserSqlId(userId); // Asignar al usuario que la adopta
        adoptedRoutine.setOriginPublicId(predefinedRoutine.getId()); // Referencia a la rutina original
        adoptedRoutine.setName(predefinedRoutine.getName() + " (Adoptada)"); // Opcional: añadir un sufijo
        adoptedRoutine.setDescription(predefinedRoutine.getDescription());
        adoptedRoutine.setDuration(predefinedRoutine.getDuration());
        adoptedRoutine.setDifficulty(predefinedRoutine.getDifficulty());
        adoptedRoutine.setCreationDate(new Date());
        // Copiar los ejercicios de la rutina predefinida
        adoptedRoutine.setExercises(predefinedRoutine.getExercises());

        return routineRepository.save(adoptedRoutine);
    }

    @Override
    public List<ObjectId> getAdoptedRoutineOriginIdsByUser(String userId) {
        return routineRepository.findByUserSqlIdAndOriginPublicIdNotNull(userId)
                .stream()
                .map(Routine::getOriginPublicId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Routine> getPredefinedRoutines() {
        // Obtener usuarios con rol "Trainer" o "Admin"
        List<User> trainers = userService.getUsersByRoleName("Trainer");
        List<User> admins = userService.getUsersByRoleName("Admin");

        // Combinar los IDs de usuario de trainers y admins
        List<String> creatorUserIds = new java.util.ArrayList<>();
        trainers.forEach(user -> creatorUserIds.add(user.getUsername()));
        admins.forEach(user -> creatorUserIds.add(user.getUsername()));

        // Buscar rutinas creadas por estos usuarios y que no sean copias (originPublicId es null)
        return routineRepository.findByUserSqlIdInAndOriginPublicIdIsNull(creatorUserIds);
    }
}
