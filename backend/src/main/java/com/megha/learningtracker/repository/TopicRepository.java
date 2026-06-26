package com.megha.learningtracker.repository;

import com.megha.learningtracker.entity.Topic;
import com.megha.learningtracker.entity.TopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<Topic> findByPhaseIdOrderBySortOrderAsc(Long phaseId);

    List<Topic> findByStatusOrderByCompletedAtDesc(TopicStatus status);

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.status = 'COMPLETED'")
    int countCompletedTopics();

    @Query("SELECT COUNT(t) FROM Topic t")
    int countTotalTopics();

    @Query("SELECT COUNT(t) FROM Topic t WHERE t.phase.id = :phaseId AND t.status = 'COMPLETED'")
    int countCompletedByPhaseId(@Param("phaseId") Long phaseId);

    @Query("SELECT COALESCE(SUM(t.xpReward), 0) FROM Topic t WHERE t.status = 'COMPLETED'")
    int sumEarnedXp();
}
