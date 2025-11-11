package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RecommendationMapper.class})
public interface ExerciseProgressMapper {

    ExerciseProgressDTO entityToDto(ExerciseProgress progress);
    ExerciseProgress dtoToEntity(ExerciseProgressDTO dto);
}