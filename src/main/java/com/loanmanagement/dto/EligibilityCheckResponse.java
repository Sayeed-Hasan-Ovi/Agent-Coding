package com.loanmanagement.dto;

import com.loanmanagement.model.ListType;
import java.time.LocalDateTime;
import java.util.List;

public class EligibilityCheckResponse {
    private String accountId;
    private boolean isEligible;
    private String message;
    private List<IneligibilityReason> ineligibilityReasons;
    private LocalDateTime checkTimestamp;
    
    public EligibilityCheckResponse() {
        this.checkTimestamp = LocalDateTime.now();
    }
    
    public EligibilityCheckResponse(String accountId, boolean isEligible, String message) {
        this();
        this.accountId = accountId;
        this.isEligible = isEligible;
        this.message = message;
    }
    
    // Getters and Setters
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
    
    public boolean isEligible() {
        return isEligible;
    }
    
    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<IneligibilityReason> getIneligibilityReasons() {
        return ineligibilityReasons;
    }
    
    public void setIneligibilityReasons(List<IneligibilityReason> ineligibilityReasons) {
        this.ineligibilityReasons = ineligibilityReasons;
    }
    
    public LocalDateTime getCheckTimestamp() {
        return checkTimestamp;
    }
    
    public void setCheckTimestamp(LocalDateTime checkTimestamp) {
        this.checkTimestamp = checkTimestamp;
    }
    
    public static class IneligibilityReason {
        private ListType listType;
        private String reason;
        private LocalDateTime addedOn;
        
        public IneligibilityReason() {}
        
        public IneligibilityReason(ListType listType, String reason, LocalDateTime addedOn) {
            this.listType = listType;
            this.reason = reason;
            this.addedOn = addedOn;
        }
        
        // Getters and Setters
        public ListType getListType() {
            return listType;
        }
        
        public void setListType(ListType listType) {
            this.listType = listType;
        }
        
        public String getReason() {
            return reason;
        }
        
        public void setReason(String reason) {
            this.reason = reason;
        }
        
        public LocalDateTime getAddedOn() {
            return addedOn;
        }
        
        public void setAddedOn(LocalDateTime addedOn) {
            this.addedOn = addedOn;
        }
    }
}
