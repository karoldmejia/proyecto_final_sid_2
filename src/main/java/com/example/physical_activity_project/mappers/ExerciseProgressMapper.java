package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ExerciseProgressDTO;
import com.example.physical_activity_project.model.ExerciseProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseProgressMapper {

    // Entity -> DTO
    ExerciseProgressDTO entityToDto(ExerciseProgress progress);

    // DTO -> Entity
    ExerciseProgress dtoToEntity(ExerciseProgressDTO dto);
}