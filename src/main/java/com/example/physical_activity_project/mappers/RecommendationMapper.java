package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.RecommendationDTO;
import com.example.physical_activity_project.model.Recommendation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    RecommendationDTO entityToDto(Recommendation recommendation);
    Recommendation dtoToEntity(RecommendationDTO dto);
}
