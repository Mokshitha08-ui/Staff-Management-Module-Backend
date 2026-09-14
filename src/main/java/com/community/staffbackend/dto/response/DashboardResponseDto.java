package com.community.staffbackend.dto.response;

public class DashboardResponseDto {

    private long totalStaff;
    private long activeStaff;
    private long inactiveStaff;
    private long currentlyInside;
    private long pendingVerification;
    private long onLeave;
    private long newThisMonth;
    private long expiringDocuments;

    public DashboardResponseDto() {
    }

    public DashboardResponseDto(long totalStaff, long activeStaff, long inactiveStaff,
                                long currentlyInside, long pendingVerification, long onLeave,
                                long newThisMonth, long expiringDocuments) {
        this.totalStaff = totalStaff;
        this.activeStaff = activeStaff;
        this.inactiveStaff = inactiveStaff;
        this.currentlyInside = currentlyInside;
        this.pendingVerification = pendingVerification;
        this.onLeave = onLeave;
        this.newThisMonth = newThisMonth;
        this.expiringDocuments = expiringDocuments;
    }

    // Getters and Setters
    public long getTotalStaff() {
        return totalStaff;
    }

    public void setTotalStaff(long totalStaff) {
        this.totalStaff = totalStaff;
    }

    public long getActiveStaff() {
        return activeStaff;
    }

    public void setActiveStaff(long activeStaff) {
        this.activeStaff = activeStaff;
    }

    public long getInactiveStaff() {
        return inactiveStaff;
    }

    public void setInactiveStaff(long inactiveStaff) {
        this.inactiveStaff = inactiveStaff;
    }

    public long getCurrentlyInside() {
        return currentlyInside;
    }

    public void setCurrentlyInside(long currentlyInside) {
        this.currentlyInside = currentlyInside;
    }

    public long getPendingVerification() {
        return pendingVerification;
    }

    public void setPendingVerification(long pendingVerification) {
        this.pendingVerification = pendingVerification;
    }

    public long getOnLeave() {
        return onLeave;
    }

    public void setOnLeave(long onLeave) {
        this.onLeave = onLeave;
    }

    public long getNewThisMonth() {
        return newThisMonth;
    }

    public void setNewThisMonth(long newThisMonth) {
        this.newThisMonth = newThisMonth;
    }

    public long getExpiringDocuments() {
        return expiringDocuments;
    }

    public void setExpiringDocuments(long expiringDocuments) {
        this.expiringDocuments = expiringDocuments;
    }
}
