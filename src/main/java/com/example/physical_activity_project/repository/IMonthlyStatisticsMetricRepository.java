package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.MonthlyStatisticsMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMonthlyStatisticsMetricRepository extends JpaRepository<MonthlyStatisticsMetric, Long> {

    Optional<MonthlyStatisticsMetric> findByStatisticsIdAndMetricName(Long statisticsId, String metricName);

}
