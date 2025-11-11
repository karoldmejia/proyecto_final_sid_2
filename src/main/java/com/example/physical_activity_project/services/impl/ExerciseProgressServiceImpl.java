package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import com.example.physical_activity_project.model.ProgressSnapshot;
import com.example.physical_activity_project.model.Routine;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ExerciseProgressServiceImpl implements IExerciseProgressService {

    @Autowired
    private IExerciseProgressRepository exerciseProgressRepository;

    @Autowired
    private IRoutineRepository routineRepository;

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

    @Override
    public List<ExerciseProgress> getProgressByUser(Long userId) {
        return exerciseProgressRepository.findByUserId(userId);
    }

    @Override
    public List<ExerciseProgress> getAllProgress() {
        return exerciseProgressRepository.findAll();
    }

    // Puedes mantener tu lógica de resumen de progreso adaptando ObjectId y Date
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

}
