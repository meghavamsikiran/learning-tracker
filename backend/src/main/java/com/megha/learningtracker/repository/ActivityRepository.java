package com.megha.learningtracker.repository;

import com.megha.learningtracker.entity.Activity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<Activity> findByActivityDateOrderByCreatedAtDesc(LocalDate date);

    @Query("SELECT a FROM Activity a WHERE a.activityDate BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<Activity> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(a.minutesSpent), 0) FROM Activity a")
    int sumTotalMinutes();

    @Query("SELECT COALESCE(SUM(a.xpEarned), 0) FROM Activity a")
    int sumTotalXpEarned();
}
