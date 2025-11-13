package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.MonthlyStatistics;
import com.example.physical_activity_project.model.MonthlyStatisticsMetric;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IMonthlyStatisticsMetricRepository;
import com.example.physical_activity_project.repository.IMonthlyStatisticsRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IMonthlyStatisticsService;
import com.example.physical_activity_project.mappers.MonthlyStatisticsMapper;
import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyStatisticsServiceImpl implements IMonthlyStatisticsService {

    @Autowired
    private IMonthlyStatisticsRepository statisticsRepository;

    @Autowired
    private IMonthlyStatisticsMetricRepository metricRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MonthlyStatisticsMapper monthlyStatisticsMapper;


    // --- Helpers ---
    public MonthlyStatistics getOrCreateStatistics(String entityId, int year, int month) {
        return statisticsRepository
                .findByEntityIdAndYearAndMonth(entityId, year, month)
                .orElseGet(() -> {
                    MonthlyStatistics stats = new MonthlyStatistics();
                    stats.setEntityId(entityId);
                    stats.setYear(year);
                    stats.setMonth(month);
                    return statisticsRepository.save(stats);
                });
    }

    private MonthlyStatisticsMetric getOrCreateMetric(MonthlyStatistics stats, String metricName) {
        return metricRepository.findByStatisticsIdAndMetricName(stats.getId(), metricName)
                .orElseGet(() -> {
                    MonthlyStatisticsMetric metric = new MonthlyStatisticsMetric();
                    metric.setStatistics(stats);
                    metric.setMetricName(metricName);
                    metric.setMetricValue(0);
                    return metricRepository.save(metric);
                });
    }

    // --- Incrementos ---
    private void incrementMetric(String entityId, String metricName) {
        LocalDate now = LocalDate.now();
        MonthlyStatistics stats = getOrCreateStatistics(entityId, now.getYear(), now.getMonthValue());
        MonthlyStatisticsMetric metric = getOrCreateMetric(stats, metricName);
        metric.setMetricValue(metric.getMetricValue() + 1);
        metricRepository.save(metric);
    }

    // Métodos públicos según rol
    @Override
    public void incrementRoutinesStarted(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!userService.hasRole(userId, "User")) return;
        incrementMetric(userId, "routines_started");
    }
    @Override
    public void incrementUserRecommendations(String userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!userService.hasRole(userId, "User")) return;
        incrementMetric(userId, "user_recommendations");
    }

    @Override
    public void incrementTrainerRecommendations(String trainerId) {
        User trainer = userRepository.findById(trainerId).orElseThrow();
        if (!userService.hasRole(trainerId, "Trainer")) return;
        incrementMetric(trainerId, "trainer_followups");
    }

    @Override
    public void incrementNewAssignments(String trainerId) {
        User trainer = userRepository.findById(trainerId).orElseThrow();
        if (!userService.hasRole(trainerId, "Trainer")) return;
        incrementMetric(trainerId, "new_assignments");
    }

    // --- Consultas ---
    private int getMetric(String entityId, String metricName, int year, int month) {
        MonthlyStatistics stats = statisticsRepository
                .findByEntityIdAndYearAndMonth(entityId, year, month)
                .orElse(null);

        if (stats == null) return 0;

        return metricRepository.findByStatisticsIdAndMetricName(stats.getId(), metricName)
                .map(MonthlyStatisticsMetric::getMetricValue)
                .orElse(0);
    }

    @Override
    public int getUserRoutinesStarted(String userId, int year, int month) {
        return getMetric(userId, "routines_started", year, month);
    }

    @Override
    public int getUserRecommendationsReceived(String userId, int year, int month) {
        return getMetric(userId, "user_recommendations", year, month);
    }

    @Override
    public int getTrainerNewAssignments(String trainerId, int year, int month) {
        return getMetric(trainerId, "new_assignments", year, month);
    }

    @Override
    public int getTrainerRecommendationsGiven(String trainerId, int year, int month) {
        return getMetric(trainerId, "trainer_followups", year, month);
    }

    @Override
    public List<MonthlyStatisticsDTO> getMonthlyStatisticsForUser(String userId) {
        return statisticsRepository.findByEntityId(userId)
                .stream()
                .map(monthlyStatisticsMapper::toDTO)
                .collect(Collectors.toList());
    }
}
