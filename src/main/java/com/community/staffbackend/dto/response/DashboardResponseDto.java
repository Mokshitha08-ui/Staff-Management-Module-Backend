package com.community.staffbackend.dto.response;

public class DashboardResponseDto {

    private long totalStaff;
    private long activeStaff;
    private long inactiveStaff;
    private long currentlyOnDuty;
    private long currentlyOffDuty;
    private long staffOnLeave;
    private long temporaryWorkers;
    private long pendingComplaints;
    private double averageRating;

    public DashboardResponseDto() {
    }

    public DashboardResponseDto(long totalStaff, long activeStaff, long inactiveStaff,
                                long currentlyOnDuty, long currentlyOffDuty, long staffOnLeave,
                                long temporaryWorkers, long pendingComplaints, double averageRating) {
        this.totalStaff = totalStaff;
        this.activeStaff = activeStaff;
        this.inactiveStaff = inactiveStaff;
        this.currentlyOnDuty = currentlyOnDuty;
        this.currentlyOffDuty = currentlyOffDuty;
        this.staffOnLeave = staffOnLeave;
        this.temporaryWorkers = temporaryWorkers;
        this.pendingComplaints = pendingComplaints;
        this.averageRating = averageRating;
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

    public long getCurrentlyOnDuty() {
        return currentlyOnDuty;
    }

    public void setCurrentlyOnDuty(long currentlyOnDuty) {
        this.currentlyOnDuty = currentlyOnDuty;
    }

    public long getCurrentlyOffDuty() {
        return currentlyOffDuty;
    }

    public void setCurrentlyOffDuty(long currentlyOffDuty) {
        this.currentlyOffDuty = currentlyOffDuty;
    }

    public long getStaffOnLeave() {
        return staffOnLeave;
    }

    public void setStaffOnLeave(long staffOnLeave) {
        this.staffOnLeave = staffOnLeave;
    }

    public long getTemporaryWorkers() {
        return temporaryWorkers;
    }

    public void setTemporaryWorkers(long temporaryWorkers) {
        this.temporaryWorkers = temporaryWorkers;
    }

    public long getPendingComplaints() {
        return pendingComplaints;
    }

    public void setPendingComplaints(long pendingComplaints) {
        this.pendingComplaints = pendingComplaints;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
