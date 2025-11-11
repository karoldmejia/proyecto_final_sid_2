package com.example.physical_activity_project.services;

import com.example.physical_activity_project.dto.ProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public interface IExerciseProgressService {

    ExerciseProgress registerProgress(Long userId, ExerciseProgress progress);

    ExerciseProgress updateProgress(ObjectId progressId, ExerciseProgress updated);

    void deleteProgress(ObjectId progressId);

    List<ExerciseProgress> getProgressByUser(Long userId);

    List<ExerciseProgress> getProgressByRoutine(ObjectId routineId);

    ProgressDTO getProgressSummary(Long userId, LocalDate startDate, LocalDate endDate);

    List<ExerciseProgress> getProgressByWeek(Long userId, LocalDate startDate);
    List<ExerciseProgress> getAllProgress();
    ExerciseProgress addRecommendation(ObjectId progressId, Long trainerId, String content);
    ExerciseProgress deleteRecommendation(ObjectId progressId, int index);
    List<LocalDate> getActiveDaysInMonth(Long userId, int year, int month);

}
