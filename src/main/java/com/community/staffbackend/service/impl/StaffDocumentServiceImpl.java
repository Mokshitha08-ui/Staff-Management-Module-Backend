package com.community.staffbackend.service.impl;

import com.community.staffbackend.dto.request.StaffDocumentCreateRequestDto;
import com.community.staffbackend.dto.response.StaffDocumentResponseDto;
import com.community.staffbackend.entity.Staff;
import com.community.staffbackend.entity.StaffDocument;
import com.community.staffbackend.entity.VerificationStatus;
import com.community.staffbackend.exception.ResourceNotFoundException;
import com.community.staffbackend.repository.StaffDocumentRepository;
import com.community.staffbackend.repository.StaffRepository;
import com.community.staffbackend.service.StaffDocumentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StaffDocumentServiceImpl implements StaffDocumentService {

    private final StaffDocumentRepository documentRepository;
    private final StaffRepository staffRepository;

    public StaffDocumentServiceImpl(StaffDocumentRepository documentRepository, StaffRepository staffRepository) {
        this.documentRepository = documentRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public StaffDocumentResponseDto addDocument(Long staffId, StaffDocumentCreateRequestDto requestDto) {
        Staff staff = staffRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found with ID: " + staffId));

        StaffDocument document = new StaffDocument();
        document.setStaff(staff);
        document.setDocumentType(requestDto.getDocumentType());
        document.setDocumentNumber(requestDto.getDocumentNumber());
        document.setDocumentPath(requestDto.getDocumentPath());
        document.setExpiryDate(requestDto.getExpiryDate());
        document.setVerificationStatus(requestDto.getVerificationStatus() != null ?
                requestDto.getVerificationStatus() : VerificationStatus.PENDING);

        StaffDocument savedDoc = documentRepository.save(document);
        return mapToDocumentResponseDto(savedDoc);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffDocumentResponseDto> getDocumentsByStaffId(Long staffId) {
        if (!staffRepository.existsById(staffId)) {
            throw new ResourceNotFoundException("Staff member not found with ID: " + staffId);
        }
        return documentRepository.findByStaffId(staffId)
                .stream()
                .map(this::mapToDocumentResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public StaffDocumentResponseDto updateVerificationStatus(Long documentId, VerificationStatus status) {
        StaffDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with ID: " + documentId));

        document.setVerificationStatus(status);
        StaffDocument updatedDoc = documentRepository.save(document);
        return mapToDocumentResponseDto(updatedDoc);
    }

    private StaffDocumentResponseDto mapToDocumentResponseDto(StaffDocument doc) {
        StaffDocumentResponseDto dto = new StaffDocumentResponseDto();
        dto.setId(doc.getId());
        dto.setStaffId(doc.getStaff().getId());
        dto.setStaffName(doc.getStaff().getFullName());
        dto.setDocumentType(doc.getDocumentType());
        dto.setDocumentNumber(doc.getDocumentNumber());
        dto.setDocumentPath(doc.getDocumentPath());
        dto.setExpiryDate(doc.getExpiryDate());
        dto.setVerificationStatus(doc.getVerificationStatus());
        dto.setUploadedAt(doc.getUploadedAt());
        return dto;
    }
}
