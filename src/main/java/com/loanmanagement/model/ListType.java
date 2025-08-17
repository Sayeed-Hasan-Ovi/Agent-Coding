package com.loanmanagement.model;

public enum ListType {
    // Ineligibility Lists
    STR("STR", "Suspicious Activity", false),
    CR("CR", "Control Report", false), 
    MULTIPLE_ACCOUNT("MULTIPLE_ACCOUNT", "Multiple Account Against ID", false),
    FDM("FDM", "Fraudulent", false),
    SST("SST", "Special Support Recommendation", false),
    
    // Delist Lists (for making accounts eligible again)
    D_STR("D_STR", "Delist STR", true),
    D_CR("D_CR", "Delist CR", true),
    D_MULTIPLE_ACCOUNT("D_MULTIPLE_ACCOUNT", "Delist Multiple Account", true),
    D_FDM("D_FDM", "Delist FDM", true),
    D_SST("D_SST", "Delist SST", true);
    
    private final String code;
    private final String description;
    private final boolean isDelist;
    
    ListType(String code, String description, boolean isDelist) {
        this.code = code;
        this.description = description;
        this.isDelist = isDelist;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isDelist() {
        return isDelist;
    }
    
    // Get the corresponding delist type for an ineligibility list
    public ListType getDelistType() {
        return switch (this) {
            case STR -> D_STR;
            case CR -> D_CR;
            case MULTIPLE_ACCOUNT -> D_MULTIPLE_ACCOUNT;
            case FDM -> D_FDM;
            case SST -> D_SST;
            default -> null;
        };
    }
    
    // Get the corresponding ineligibility type for a delist
    public ListType getIneligibilityType() {
        return switch (this) {
            case D_STR -> STR;
            case D_CR -> CR;
            case D_MULTIPLE_ACCOUNT -> MULTIPLE_ACCOUNT;
            case D_FDM -> FDM;
            case D_SST -> SST;
            default -> null;
        };
    }
}
