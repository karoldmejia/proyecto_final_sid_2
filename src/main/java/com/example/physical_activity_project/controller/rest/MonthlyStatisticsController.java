package com.example.physical_activity_project.controller.rest;

import com.example.physical_activity_project.dto.MonthlyStatisticsDTO;
import com.example.physical_activity_project.mappers.MonthlyStatisticsMapper;
import com.example.physical_activity_project.model.MonthlyStatistics;
import com.example.physical_activity_project.services.impl.MonthlyStatisticsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class MonthlyStatisticsController {

    @Autowired
    private MonthlyStatisticsServiceImpl statisticsService;

    @GetMapping("/user/{userId}")
    public MonthlyStatisticsDTO getUserStatistics(
            @PathVariable String userId,
            @RequestParam int year,
            @RequestParam int month) {

        MonthlyStatistics stats = statisticsService.getOrCreateStatistics(userId, year, month);
        return MonthlyStatisticsMapper.INSTANCE.toDTO(stats);
    }

    @GetMapping("/trainer/{trainerId}")
    public MonthlyStatisticsDTO getTrainerStatistics(
            @PathVariable String trainerId,
            @RequestParam int year,
            @RequestParam int month) {

        MonthlyStatistics stats = statisticsService.getOrCreateStatistics(trainerId, year, month);
        return MonthlyStatisticsMapper.INSTANCE.toDTO(stats);
    }
}
