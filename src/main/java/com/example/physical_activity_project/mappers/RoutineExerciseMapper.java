package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.RoutineExerciseDTO;
import com.example.physical_activity_project.model.RoutineExercise;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoutineExerciseMapper {

    RoutineExerciseDTO entityToDto(RoutineExercise entity);

    RoutineExercise dtoToEntity(RoutineExerciseDTO dto);
}
