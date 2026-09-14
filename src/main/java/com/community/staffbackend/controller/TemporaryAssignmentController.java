package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.service.TemporaryAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/temporary-workers")
public class TemporaryAssignmentController {

    private final TemporaryAssignmentService assignmentService;

    public TemporaryAssignmentController(TemporaryAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<TemporaryAssignmentResponseDto>> getAssignments(
            @RequestParam(required = false) String status) {
        List<TemporaryAssignmentResponseDto> assignments = assignmentService.getAssignments(status);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/assign")
    public ResponseEntity<TemporaryAssignmentResponseDto> assignReplacement(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(assignmentService.assignReplacement(body));
    }

    @GetMapping("/active")
    public ResponseEntity<List<TemporaryAssignmentResponseDto>> getActiveReplacements() {
        return ResponseEntity.ok(assignmentService.getActiveReplacements());
    }

    @GetMapping("/history")
    public ResponseEntity<List<TemporaryAssignmentResponseDto>> getReplacementHistory(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(assignmentService.getReplacementHistory(search, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemporaryAssignmentResponseDto> getAssignmentById(@PathVariable Long id) {
        TemporaryAssignmentResponseDto assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    @PostMapping
    public ResponseEntity<TemporaryAssignmentResponseDto> createAssignment(
            @Valid @RequestBody TemporaryAssignmentCreateRequestDto requestDto) {
        TemporaryAssignmentResponseDto created = assignmentService.createAssignment(requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemporaryAssignmentResponseDto> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody TemporaryAssignmentUpdateRequestDto requestDto) {
        TemporaryAssignmentResponseDto updated = assignmentService.updateAssignment(id, requestDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Long id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }
}
