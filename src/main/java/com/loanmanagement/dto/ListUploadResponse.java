package com.loanmanagement.dto;

import com.loanmanagement.model.ListType;
import java.time.LocalDateTime;

public class ListUploadResponse {
    private ListType listType;
    private int totalRecords;
    private int processedRecords;
    private int skippedRecords;
    private LocalDateTime uploadTimestamp;
    private String message;
    private boolean success;
    
    public ListUploadResponse() {
        this.uploadTimestamp = LocalDateTime.now();
    }
    
    public ListUploadResponse(ListType listType, boolean success, String message) {
        this();
        this.listType = listType;
        this.success = success;
        this.message = message;
    }
    
    // Getters and Setters
    public ListType getListType() {
        return listType;
    }
    
    public void setListType(ListType listType) {
        this.listType = listType;
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
    
    public int getProcessedRecords() {
        return processedRecords;
    }
    
    public void setProcessedRecords(int processedRecords) {
        this.processedRecords = processedRecords;
    }
    
    public int getSkippedRecords() {
        return skippedRecords;
    }
    
    public void setSkippedRecords(int skippedRecords) {
        this.skippedRecords = skippedRecords;
    }
    
    public LocalDateTime getUploadTimestamp() {
        return uploadTimestamp;
    }
    
    public void setUploadTimestamp(LocalDateTime uploadTimestamp) {
        this.uploadTimestamp = uploadTimestamp;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
