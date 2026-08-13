package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.TemporaryAssignmentCreateRequestDto;
import com.community.staffbackend.dto.request.TemporaryAssignmentUpdateRequestDto;
import com.community.staffbackend.dto.response.TemporaryAssignmentResponseDto;
import com.community.staffbackend.entity.ReplacementStatus;
import com.community.staffbackend.service.TemporaryAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/temporary-workers")
public class TemporaryAssignmentController {

    private final TemporaryAssignmentService assignmentService;

    public TemporaryAssignmentController(TemporaryAssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @GetMapping
    public ResponseEntity<List<TemporaryAssignmentResponseDto>> getAssignments(
            @RequestParam(required = false) ReplacementStatus status) {
        List<TemporaryAssignmentResponseDto> assignments = assignmentService.getAssignments(status);
        return ResponseEntity.ok(assignments);
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
