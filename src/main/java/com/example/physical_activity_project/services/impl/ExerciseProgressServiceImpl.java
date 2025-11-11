package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.dto.RoutineProgressDTO;
import com.example.physical_activity_project.model.*;
import com.example.physical_activity_project.repository.IExerciseProgressRepository;
import com.example.physical_activity_project.repository.IRoutineRepository;
import com.example.physical_activity_project.services.IExerciseProgressService;
import com.example.physical_activity_project.dto.ProgressDTO;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExerciseProgressServiceImpl implements IExerciseProgressService {

    @Autowired
    private IExerciseProgressRepository exerciseProgressRepository;

    @Autowired
    private IRoutineRepository routineRepository;

    @Autowired
    private UserTrainerAssignmentServiceImpl assignmentService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ExerciseProgress registerProgress(Long userId, ExerciseProgress progress) {
        progress.setUserId(userId);
        ExerciseProgress saved = exerciseProgressRepository.save(progress);

        ProgressSnapshot snapshot = new ProgressSnapshot(
                saved.getProgressDate(),
                saved.getSetsCompleted(),
                saved.getRepsCompleted(),
                saved.getTimeCompleted(),
                saved.getEffortLevel()
        );
        Query query = new Query(Criteria.where("exercises._id").is(progress.getRoutineExerciseId()));
        Document pushOperation = new Document("$each", Collections.singletonList(snapshot))
                .append("$slice", -15);

        Update update = new Update().push("exercises.$.recentProgresses", pushOperation);
        mongoTemplate.updateFirst(query, update, Routine.class);

        return saved;
    }

    @Override
    public ExerciseProgress updateProgress(ObjectId progressId, ExerciseProgress updated) {
        Optional<ExerciseProgress> optional = exerciseProgressRepository.findById(progressId);

        if (optional.isPresent()) {
            ExerciseProgress existing = optional.get();
            existing.setProgressDate(updated.getProgressDate());
            existing.setSetsCompleted(updated.getSetsCompleted());
            existing.setRepsCompleted(updated.getRepsCompleted());
            existing.setTimeCompleted(updated.getTimeCompleted());
            existing.setEffortLevel(updated.getEffortLevel());
            existing.setRoutineExerciseId(updated.getRoutineExerciseId());
            return exerciseProgressRepository.save(existing);
        } else {
            throw new RuntimeException("Progress not found with id: " + progressId);
        }
    }

    @Override
    public void deleteProgress(ObjectId progressId) {
        if (exerciseProgressRepository.existsById(progressId)) {
            exerciseProgressRepository.deleteById(progressId);
        } else {
            throw new RuntimeException("Cannot delete. Progress not found with id: " + progressId);
        }
    }

    public ExerciseProgress getProgressById(ObjectId id) {
        return exerciseProgressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Progress not found with id: " + id));
    }

    @Override
    public List<ExerciseProgress> getProgressByUser(Long userId) {
        return exerciseProgressRepository.findByUserId(userId);
    }

    @Override
    public List<ExerciseProgress> getAllProgress() {
        return exerciseProgressRepository.findAll();
    }

    @Override
    public ProgressDTO getProgressSummary(Long userId, LocalDate start, LocalDate end) {
        List<ExerciseProgress> progresses = getProgressInRange(userId, start, end);

        long totalExercises = progresses.size();
        int totalSets = progresses.stream().mapToInt(p -> p.getSetsCompleted() != null ? p.getSetsCompleted() : 0).sum();
        int totalReps = progresses.stream().mapToInt(p -> p.getRepsCompleted() != null ? p.getRepsCompleted() : 0).sum();
        int totalTime = progresses.stream().mapToInt(p -> p.getTimeCompleted() != null ? p.getTimeCompleted() : 0).sum();
        double avgEffort = progresses.stream().mapToInt(p -> p.getEffortLevel() != null ? p.getEffortLevel() : 0).average().orElse(0.0);

        return new ProgressDTO(totalExercises, totalSets, totalReps, totalTime, avgEffort);
    }

    @Override
    public List<ExerciseProgress> getProgressByWeek(Long userId, LocalDate startDate) {
        LocalDate endDate = startDate.plusDays(6);
        return getProgressInRange(userId, startDate, endDate);
    }

    private List<ExerciseProgress> getProgressInRange(Long userId, LocalDate start, LocalDate end) {
        Date startDate = java.sql.Date.valueOf(start);
        Date endDate = java.sql.Date.valueOf(end.plusDays(1)); // incluir último día
        return exerciseProgressRepository.findByUserIdAndProgressDateBetween(userId, startDate, endDate);
    }
    @Override
    public List<ExerciseProgress> getProgressByRoutine(ObjectId routineId) {
        Optional<Routine> routineOpt = routineRepository.findById(routineId);
        if (routineOpt.isEmpty()) {
            throw new RuntimeException("Routine not found with id: " + routineId);
        }

        Routine routine = routineOpt.get();
        List<ObjectId> routineExerciseIds = routine.getExercises().stream()
                .map(re -> re.getExerciseId())
                .toList();
        return exerciseProgressRepository.findByRoutineExerciseIds(routineExerciseIds);
    }

    @Override
    public List<LocalDate> getActiveDaysInMonth(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<ExerciseProgress> progresses = getProgressInRange(userId, start, end);

        List<LocalDate> uniqueDays = progresses.stream()
                .map(p -> p.getProgressDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate())
                .distinct()
                .sorted()
                .toList();

        return uniqueDays;
    }

    public RoutineProgressDTO getRoutineProgressSummary(ObjectId routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        List<ExerciseProgress> progresses = getProgressByRoutine(routineId);

        int totalSets = progresses.stream().mapToInt(p -> p.getSetsCompleted() != null ? p.getSetsCompleted() : 0).sum();
        int totalReps = progresses.stream().mapToInt(p -> p.getRepsCompleted() != null ? p.getRepsCompleted() : 0).sum();
        int totalTime = progresses.stream().mapToInt(p -> p.getTimeCompleted() != null ? p.getTimeCompleted() : 0).sum();
        double avgEffort = progresses.stream().mapToInt(p -> p.getEffortLevel() != null ? p.getEffortLevel() : 0).average().orElse(0.0);

        RoutineProgressDTO dto = new RoutineProgressDTO();
        dto.setRoutineName(routine.getName());
        dto.setTotalExercises(progresses.size());
        dto.setTotalSets(totalSets);
        dto.setTotalReps(totalReps);
        dto.setTotalTime(totalTime);
        dto.setAvgEffort(avgEffort);

        return dto;
    }


    // Métodos para recomendaciones

    @Override
    public ExerciseProgress addRecommendation(ObjectId progressId, Long trainerId, String content) {
        ExerciseProgress progress = mongoTemplate.findById(progressId, ExerciseProgress.class);
        if (progress == null) {
            throw new RuntimeException("No se encontró el progreso con ID: " + progressId);
        }

        validateTrainer(trainerId, progress);

        Recommendation recommendation = new Recommendation();
        recommendation.setTrainerSqlId(trainerId);
        recommendation.setContent(content);
        recommendation.setRecommendationDate(LocalDateTime.now());

        Query query = new Query(Criteria.where("_id").is(progressId));
        Update update = new Update().push("recommendations", recommendation);
        mongoTemplate.updateFirst(query, update, ExerciseProgress.class);
        return mongoTemplate.findOne(query, ExerciseProgress.class);
    }

    @Override
    public ExerciseProgress deleteRecommendation(ObjectId progressId, int index) {
        Query query = new Query(Criteria.where("_id").is(progressId));
        query.fields().include("recommendations");

        ExerciseProgress progress = mongoTemplate.findOne(query, ExerciseProgress.class);
        if (progress == null || progress.getRecommendations() == null || index >= progress.getRecommendations().size()) {
            throw new RuntimeException("Recommendation not found at index " + index);
        }

        progress.getRecommendations().remove(index);
        Update update = new Update().set("recommendations", progress.getRecommendations());
        mongoTemplate.updateFirst(query, update, ExerciseProgress.class);

        return mongoTemplate.findOne(new Query(Criteria.where("_id").is(progressId)), ExerciseProgress.class);
    }

    private void validateTrainer(Long trainerId, ExerciseProgress progress) {
        ObjectId routineExerciseId = progress.getRoutineExerciseId();
        if (routineExerciseId == null) {
            throw new RuntimeException("El progreso no tiene un RoutineExercise asociado.");
        }

        Routine routine = mongoTemplate.findOne(
                Query.query(Criteria.where("exercises").elemMatch(
                        Criteria.where("_id").is(routineExerciseId)
                )),
                Routine.class
        );

        if (routine == null) {
            throw new RuntimeException("No se encontró una rutina que contenga el RoutineExercise con ID: " + routineExerciseId);
        }

        Long userSqlId = routine.getUserSqlId();
        if (userSqlId == null) {
            throw new RuntimeException("La rutina no tiene un usuario asociado (userSqlId).");
        }

        boolean isAssigned = assignmentService.validateTrainer(trainerId, userSqlId);
        if (!isAssigned) {
            throw new RuntimeException("El entrenador no está asignado al usuario de este progreso.");
        }
    }

}
