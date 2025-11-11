package com.example.physical_activity_project.repository;

import com.example.physical_activity_project.model.MonthlyStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IMonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {

    Optional<MonthlyStatistics> findByEntityIdAndYearAndMonth(
            Long entityId, Integer year, Integer month
    );

}
