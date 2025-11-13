package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.mappers.ExerciseMapper;
import com.example.physical_activity_project.model.Exercise;
import com.example.physical_activity_project.repository.IExerciseRepository;
import com.example.physical_activity_project.services.IExerciseService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExerciseServiceImpl implements IExerciseService {

    @Autowired
    private IExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Override
    public Exercise createExercise(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    public Exercise updateExercise(ObjectId id, Exercise updatedExercise) {
        Optional<Exercise> optionalExercise = exerciseRepository.findById(id);

        if (optionalExercise.isPresent()) {
            Exercise existingExercise = optionalExercise.get();
            existingExercise.setName(updatedExercise.getName());
            existingExercise.setType(updatedExercise.getType());
            existingExercise.setDescription(updatedExercise.getDescription());
            existingExercise.setDuration(updatedExercise.getDuration());
            existingExercise.setDifficulty(updatedExercise.getDifficulty());
            existingExercise.setVideoUrl(updatedExercise.getVideoUrl());
            return exerciseRepository.save(existingExercise);
        } else {
            throw new RuntimeException("Exercise not found with id: " + id);
        }
    }

    @Override
    public void deleteExercise(ObjectId id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Cannot delete. Exercise not found with id: " + id);
        }
    }

    @Override
    public Exercise getExerciseById(ObjectId id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercise not found with id: " + id));
    }

    @Override
    public List<Exercise> getAllExercises() {
        return exerciseRepository.findAll();
    }

    @Override
    public List<Exercise> getExercisesByType(String type) {
        return exerciseRepository.findByType(type);
    }

    @Override
    public List<Exercise> getExercisesByDifficulty(String difficulty) {
        return exerciseRepository.findByDifficulty(difficulty);
    }

    @Override
    public Exercise createExercise(String userId, ExerciseDTO exerciseDTO) {
        Exercise exercise = exerciseMapper.dtoToEntity(exerciseDTO);
        exercise.setUserSqlId(userId);
        return exerciseRepository.save(exercise);
    }
}
