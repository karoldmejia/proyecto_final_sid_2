package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.model.Exercise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    ExerciseDTO entityToDto(Exercise exercise);
    Exercise dtoToEntity(ExerciseDTO exerciseDTO);
}
