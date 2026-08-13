package com.community.staffbackend.dto.request;

import com.community.staffbackend.entity.DocumentType;
import com.community.staffbackend.entity.VerificationStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class StaffDocumentCreateRequestDto {

    @NotNull(message = "Document type is required")
    private DocumentType documentType;

    private String documentNumber;

    private String documentPath; // Placeholder string path/url

    private LocalDate expiryDate;

    private VerificationStatus verificationStatus;

    public StaffDocumentCreateRequestDto() {
    }

    // Getters and Setters
    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(VerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
