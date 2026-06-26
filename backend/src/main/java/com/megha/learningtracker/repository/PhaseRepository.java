package com.megha.learningtracker.repository;

import com.megha.learningtracker.entity.Phase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {

    Optional<Phase> findByPhaseNumber(int phaseNumber);

    List<Phase> findAllByOrderByPhaseNumberAsc();

    @Query("SELECT COUNT(p) FROM Phase p WHERE p.status = 'COMPLETED'")
    int countCompletedPhases();

    boolean existsByPhaseNumber(int phaseNumber);
}
