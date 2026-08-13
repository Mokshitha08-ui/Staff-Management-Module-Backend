package com.community.staffbackend.service;

import com.community.staffbackend.dto.request.StaffDocumentCreateRequestDto;
import com.community.staffbackend.dto.response.StaffDocumentResponseDto;
import com.community.staffbackend.entity.VerificationStatus;

import java.util.List;

public interface StaffDocumentService {
    StaffDocumentResponseDto addDocument(Long staffId, StaffDocumentCreateRequestDto requestDto);
    List<StaffDocumentResponseDto> getDocumentsByStaffId(Long staffId);
    StaffDocumentResponseDto updateVerificationStatus(Long documentId, VerificationStatus status);
}
