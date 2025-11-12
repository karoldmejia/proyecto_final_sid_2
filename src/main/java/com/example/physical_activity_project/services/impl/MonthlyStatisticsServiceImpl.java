package com.example.physical_activity_project.services.impl;

import com.example.physical_activity_project.model.MonthlyStatistics;
import com.example.physical_activity_project.model.MonthlyStatisticsMetric;
import com.example.physical_activity_project.model.User;
import com.example.physical_activity_project.repository.IMonthlyStatisticsMetricRepository;
import com.example.physical_activity_project.repository.IMonthlyStatisticsRepository;
import com.example.physical_activity_project.repository.IUserRepository;
import com.example.physical_activity_project.services.IMonthlyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class MonthlyStatisticsServiceImpl implements IMonthlyStatisticsService {

    @Autowired
    private IMonthlyStatisticsRepository statisticsRepository;

    @Autowired
    private IMonthlyStatisticsMetricRepository metricRepository;

    @Autowired
    private IUserRepository userRepository;

    // --- Helpers ---
    public MonthlyStatistics getOrCreateStatistics(Long entityId, int year, int month) {
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
    private void incrementMetric(Long entityId, String metricName) {
        LocalDate now = LocalDate.now();
        MonthlyStatistics stats = getOrCreateStatistics(entityId, now.getYear(), now.getMonthValue());
        MonthlyStatisticsMetric metric = getOrCreateMetric(stats, metricName);
        metric.setMetricValue(metric.getMetricValue() + 1);
        metricRepository.save(metric);
    }

    // Métodos públicos según rol
    @Override
    public void incrementRoutinesStarted(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!user.getRole().getName().equals("User")) return; // Solo para usuarios
        incrementMetric(userId, "routines_started");
    }
    @Override
    public void incrementUserRecommendations(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (!user.getRole().getName().equals("User")) return;
        incrementMetric(userId, "user_recommendations");
    }

    @Override
    public void incrementTrainerRecommendations(Long trainerId) {
        User trainer = userRepository.findById(trainerId).orElseThrow();
        if (!trainer.getRole().getName().equals("Trainer")) return;
        incrementMetric(trainerId, "trainer_followups");
    }

    @Override
    public void incrementNewAssignments(Long trainerId) {
        User trainer = userRepository.findById(trainerId).orElseThrow();
        if (!trainer.getRole().getName().equals("Trainer")) return;
        incrementMetric(trainerId, "new_assignments");
    }

    // --- Consultas ---
    private int getMetric(Long entityId, String metricName, int year, int month) {
        MonthlyStatistics stats = statisticsRepository
                .findByEntityIdAndYearAndMonth(entityId, year, month)
                .orElse(null);

        if (stats == null) return 0;

        return metricRepository.findByStatisticsIdAndMetricName(stats.getId(), metricName)
                .map(MonthlyStatisticsMetric::getMetricValue)
                .orElse(0);
    }

    @Override
    public int getUserRoutinesStarted(Long userId, int year, int month) {
        return getMetric(userId, "routines_started", year, month);
    }

    @Override
    public int getUserRecommendationsReceived(Long userId, int year, int month) {
        return getMetric(userId, "user_recommendations", year, month);
    }

    @Override
    public int getTrainerNewAssignments(Long trainerId, int year, int month) {
        return getMetric(trainerId, "new_assignments", year, month);
    }

    @Override
    public int getTrainerRecommendationsGiven(Long trainerId, int year, int month) {
        return getMetric(trainerId, "trainer_followups", year, month);
    }
}
