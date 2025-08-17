package com.loanmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public class EligibilityCheckRequest {
    @NotBlank(message = "Account ID is required")
    private String accountId;
    
    public EligibilityCheckRequest() {}
    
    public EligibilityCheckRequest(String accountId) {
        this.accountId = accountId;
    }
    
    public String getAccountId() {
        return accountId;
    }
    
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
