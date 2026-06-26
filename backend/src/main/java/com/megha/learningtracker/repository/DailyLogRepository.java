package com.megha.learningtracker.repository;

import com.megha.learningtracker.entity.DailyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    Optional<DailyLog> findByLogDate(LocalDate logDate);

    @Query("SELECT d FROM DailyLog d WHERE d.logDate BETWEEN :startDate AND :endDate ORDER BY d.logDate ASC")
    List<DailyLog> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT d FROM DailyLog d WHERE d.logDate >= :startDate ORDER BY d.logDate DESC")
    List<DailyLog> findFromDate(@Param("startDate") LocalDate startDate);

    @Query("SELECT d FROM DailyLog d ORDER BY d.logDate DESC")
    List<DailyLog> findAllOrderByDateDesc();

    @Query("SELECT COALESCE(SUM(d.totalMinutes), 0) FROM DailyLog d")
    int sumTotalMinutes();

    @Query("SELECT COUNT(d) FROM DailyLog d WHERE d.totalMinutes > 0 OR d.topicsCompleted > 0")
    int countActiveDays();
}
