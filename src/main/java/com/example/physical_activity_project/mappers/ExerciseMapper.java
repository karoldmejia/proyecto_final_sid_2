package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.ExerciseDTO;
import com.example.physical_activity_project.model.Exercise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ObjectIdMapper.class)
public interface ExerciseMapper {



        @Mapping(target = "id", source = "id")
        ExerciseDTO entityToDto(Exercise exercise);

        @Mapping(target = "id", source = "id")
        Exercise dtoToEntity(ExerciseDTO exerciseDTO);
    }

