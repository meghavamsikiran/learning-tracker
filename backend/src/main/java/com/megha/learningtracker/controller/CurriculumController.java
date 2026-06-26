package com.megha.learningtracker.controller;

import com.megha.learningtracker.dto.PhaseDTO;
import com.megha.learningtracker.dto.TopicDTO;
import com.megha.learningtracker.dto.TopicUpdateRequest;
import com.megha.learningtracker.service.CurriculumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
@RequiredArgsConstructor
public class CurriculumController {

    private final CurriculumService curriculumService;

    /**
     * GET /api/curriculum
     * Returns all phases with their topics and progress.
     */
    @GetMapping
    public ResponseEntity<List<PhaseDTO>> getAllPhases() {
        return ResponseEntity.ok(curriculumService.getAllPhases());
    }

    /**
     * GET /api/curriculum/phases/{id}
     * Returns a single phase with all its topics.
     */
    @GetMapping("/phases/{id}")
    public ResponseEntity<PhaseDTO> getPhase(@PathVariable Long id) {
        return ResponseEntity.ok(curriculumService.getPhaseById(id));
    }

    /**
     * PUT /api/curriculum/topics/{id}
     * Update a topic's status and/or notes.
     */
    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicDTO> updateTopic(
            @PathVariable Long id,
            @RequestBody TopicUpdateRequest request) {
        return ResponseEntity.ok(curriculumService.updateTopic(id, request));
    }

    /**
     * POST /api/curriculum/topics/{id}/complete
     * Mark a topic as completed. Awards XP and updates phase progress.
     */
    @PostMapping("/topics/{id}/complete")
    public ResponseEntity<TopicDTO> completeTopic(
            @PathVariable Long id,
            @RequestParam(required = false) Integer minutesSpent) {
        return ResponseEntity.ok(curriculumService.completeTopic(id, minutesSpent));
    }
}
