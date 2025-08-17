package com.loanmanagement.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class EligibilityRecord {
    private String accountId;
    private ListType listType;
    private LocalDateTime uploadTimestamp;
    private String reason;
    private boolean isActive;
    
    public EligibilityRecord() {}
    
    public EligibilityRecord(String accountId, ListType listType, LocalDateTime uploadTimestamp, String reason) {
        this.accountId = accountId;
        this.listType = listType;
        this.uploadTimestamp = uploadTimestamp;
        this.reason = reason;
        this.isActive = true;
    }
    
    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public ListType getListType() {
        return listType;
    }
    
    public void setListType(ListType listType) {
        this.listType = listType;
    }
    
    public LocalDateTime getUploadTimestamp() {
        return uploadTimestamp;
    }
    
    public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EligibilityRecord that = (EligibilityRecord) o;
        return Objects.equals(accountId, that.accountId) &&
               listType == that.listType &&
               Objects.equals(uploadTimestamp, that.uploadTimestamp);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(accountId, listType, uploadTimestamp);
    }
    
    @Override
    public String toString() {
        return "EligibilityRecord{" +
                "accountId='" + accountId + '\'' +
                ", listType=" + listType +
                ", uploadTimestamp=" + uploadTimestamp +
                ", reason='" + reason + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
