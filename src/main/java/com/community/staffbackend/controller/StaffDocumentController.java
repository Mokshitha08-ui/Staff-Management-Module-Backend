package com.community.staffbackend.controller;

import com.community.staffbackend.dto.request.StaffDocumentCreateRequestDto;
import com.community.staffbackend.dto.response.StaffDocumentResponseDto;
import com.community.staffbackend.entity.VerificationStatus;
import com.community.staffbackend.service.StaffDocumentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class StaffDocumentController {

    private final StaffDocumentService documentService;

    public StaffDocumentController(StaffDocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/api/staff/{id}/documents")
    public ResponseEntity<List<StaffDocumentResponseDto>> getDocumentsByStaffId(@PathVariable Long id) {
        List<StaffDocumentResponseDto> documents = documentService.getDocumentsByStaffId(id);
        return ResponseEntity.ok(documents);
    }

    @PostMapping("/api/staff/{id}/documents")
    public ResponseEntity<StaffDocumentResponseDto> addDocument(
            @PathVariable Long id,
            @Valid @RequestBody StaffDocumentCreateRequestDto requestDto) {
        StaffDocumentResponseDto created = documentService.addDocument(id, requestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PatchMapping("/api/documents/{id}/status")
    public ResponseEntity<StaffDocumentResponseDto> updateVerificationStatus(
            @PathVariable Long id,
            @RequestParam VerificationStatus status) {
        StaffDocumentResponseDto updated = documentService.updateVerificationStatus(id, status);
        return ResponseEntity.ok(updated);
    }
}
