package com.example.physical_activity_project.mappers;

import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.model.MonthlyStatistics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MonthlyStatisticsMapper {

    MonthlyStatisticsMapper INSTANCE = Mappers.getMapper(MonthlyStatisticsMapper.class);

    @Mapping(source = "metrics", target = "routinesStarted", qualifiedByName = "getMetricValueRoutines")
    @Mapping(source = "metrics", target = "userRecommendations", qualifiedByName = "getMetricValueUserRecommendations")
    @Mapping(source = "metrics", target = "newAssignments", qualifiedByName = "getMetricValueNewAssignments")
    @Mapping(source = "metrics", target = "trainerFollowups", qualifiedByName = "getMetricValueTrainerFollowups")
    MonthlyStatisticsDTO toDTO(MonthlyStatistics stats);

    @Named("getMetricValueRoutines")
    static Integer getMetricValueRoutines(List<com.example.physical_activity_project.model.MonthlyStatisticsMetric> metrics) {
        return metrics.stream()
                .filter(m -> "routines_started".equals(m.getMetricName()))
                .map(com.example.physical_activity_project.model.MonthlyStatisticsMetric::getMetricValue)
                .findFirst().orElse(0);
    }

    @Named("getMetricValueUserRecommendations")
    static Integer getMetricValueUserRecommendations(List<com.example.physical_activity_project.model.MonthlyStatisticsMetric> metrics) {
        return metrics.stream()
                .filter(m -> "user_recommendations".equals(m.getMetricName()))
                .map(com.example.physical_activity_project.model.MonthlyStatisticsMetric::getMetricValue)
                .findFirst().orElse(0);
    }

    @Named("getMetricValueNewAssignments")
    static Integer getMetricValueNewAssignments(List<com.example.physical_activity_project.model.MonthlyStatisticsMetric> metrics) {
        return metrics.stream()
                .filter(m -> "new_assignments".equals(m.getMetricName()))
                .map(com.example.physical_activity_project.model.MonthlyStatisticsMetric::getMetricValue)
                .findFirst().orElse(0);
    }

    @Named("getMetricValueTrainerFollowups")
    static Integer getMetricValueTrainerFollowups(List<com.example.physical_activity_project.model.MonthlyStatisticsMetric> metrics) {
        return metrics.stream()
                .filter(m -> "trainer_followups".equals(m.getMetricName()))
                .map(com.example.physical_activity_project.model.MonthlyStatisticsMetric::getMetricValue)
                .findFirst().orElse(0);
    }
}
