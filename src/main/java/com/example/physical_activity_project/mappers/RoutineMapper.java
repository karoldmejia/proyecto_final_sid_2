package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.RoutineDTO;
import com.example.physical_activity_project.model.Routine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RoutineExerciseMapper.class})
public interface RoutineMapper {

    RoutineDTO entityToDto(Routine routine);

    Routine dtoToEntity(RoutineDTO routineDTO);
}
