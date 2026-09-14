package com.community.staffbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_temporary_assignment")
public class TemporaryAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_code")
    private String assignmentCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regular_staff_id", nullable = false)
    private Staff regularStaff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replacement_staff_id", nullable = false)
    private Staff replacementStaff;

    @Column(name = "replacement_phone")
    private String replacementPhone;

    @Column(name = "is_temporary_worker")
    private Boolean isTemporaryWorker = false;

    @Column(name = "reason")
    private String reason;

    @Column(name = "tower_assigned")
    private String towerAssigned;

    @Column(name = "block_assigned")
    private String blockAssigned;

    @Column(name = "shift")
    private String shift;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_until_further_notice")
    private Boolean isUntilFurtherNotice = false;

    @Column(name = "status", nullable = false)
    private String status; // active, completed, cancelled

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TemporaryAssignment() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "active";
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignmentCode() {
        return assignmentCode;
    }

    public void setAssignmentCode(String assignmentCode) {
        this.assignmentCode = assignmentCode;
    }

    public Staff getRegularStaff() {
        return regularStaff;
    }

    public void setRegularStaff(Staff regularStaff) {
        this.regularStaff = regularStaff;
    }

    public Staff getReplacementStaff() {
        return replacementStaff;
    }

    public void setReplacementStaff(Staff replacementStaff) {
        this.replacementStaff = replacementStaff;
    }

    public String getReplacementPhone() {
        return replacementPhone;
    }

    public void setReplacementPhone(String replacementPhone) {
        this.replacementPhone = replacementPhone;
    }

    public Boolean getIsTemporaryWorker() {
        return isTemporaryWorker;
    }

    public void setIsTemporaryWorker(Boolean temporaryWorker) {
        isTemporaryWorker = temporaryWorker;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getTowerAssigned() {
        return towerAssigned;
    }

    public void setTowerAssigned(String towerAssigned) {
        this.towerAssigned = towerAssigned;
    }

    public String getBlockAssigned() {
        return blockAssigned;
    }

    public void setBlockAssigned(String blockAssigned) {
        this.blockAssigned = blockAssigned;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsUntilFurtherNotice() {
        return isUntilFurtherNotice;
    }

    public void setIsUntilFurtherNotice(Boolean untilFurtherNotice) {
        isUntilFurtherNotice = untilFurtherNotice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
