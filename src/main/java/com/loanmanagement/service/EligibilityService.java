package com.loanmanagement.service;

import com.loanmanagement.dto.EligibilityCheckResponse;
import com.loanmanagement.dto.ListUploadResponse;
import com.loanmanagement.model.EligibilityRecord;
import com.loanmanagement.model.ListType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class EligibilityService {
    
    // In-memory storage for all eligibility records
    private final Map<String, Map<ListType, EligibilityRecord>> eligibilityData = new ConcurrentHashMap<>();
    
    /**
     * Upload and process a list (CSV format)
     * Expected CSV format: AccountID,Reason
     */
    public ListUploadResponse uploadList(ListType listType, MultipartFile file) {
        ListUploadResponse response = new ListUploadResponse(listType, false, "");
        LocalDateTime uploadTimestamp = LocalDateTime.now();
        
        try {
            Reader reader = new InputStreamReader(file.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withHeader("AccountID", "Reason")
                .parse(reader);
            
            int totalRecords = 0;
            int processedRecords = 0;
            int skippedRecords = 0;
            
            for (CSVRecord record : records) {
                totalRecords++;
                
                try {
                    String accountId = record.get("AccountID").trim();
                    String reason = record.get("Reason") != null ? record.get("Reason").trim() : "";
                    
                    if (accountId.isEmpty()) {
                        skippedRecords++;
                        continue;
                    }
                    
                    processRecord(accountId, listType, uploadTimestamp, reason);
                    processedRecords++;
                    
                } catch (Exception e) {
                    skippedRecords++;
                }
            }
            
            response.setTotalRecords(totalRecords);
            response.setProcessedRecords(processedRecords);
            response.setSkippedRecords(skippedRecords);
            response.setSuccess(true);
            response.setMessage(String.format("Successfully processed %d out of %d records for %s", 
                processedRecords, totalRecords, listType.getDescription()));
            
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error processing file: " + e.getMessage());
        }
        
        return response;
    }
    
    /**
     * Process a single record based on list type
     */
    private void processRecord(String accountId, ListType listType, LocalDateTime uploadTimestamp, String reason) {
        eligibilityData.computeIfAbsent(accountId, k -> new ConcurrentHashMap<>());
        
        if (listType.isDelist()) {
            // Handle delist operation
            processDelistRecord(accountId, listType, uploadTimestamp, reason);
        } else {
            // Handle ineligibility list addition
            processIneligibilityRecord(accountId, listType, uploadTimestamp, reason);
        }
    }
    
    /**
     * Process ineligibility record (STR, CR, MULTIPLE_ACCOUNT, FDM, SST)
     */
    private void processIneligibilityRecord(String accountId, ListType listType, LocalDateTime uploadTimestamp, String reason) {
        Map<ListType, EligibilityRecord> accountRecords = eligibilityData.get(accountId);
        
        // Check if there's an existing record for this list type
        EligibilityRecord existingRecord = accountRecords.get(listType);
        
        if (existingRecord == null || uploadTimestamp.isAfter(existingRecord.getUploadTimestamp())) {
            // Add or update the record
            EligibilityRecord newRecord = new EligibilityRecord(accountId, listType, uploadTimestamp, reason);
            accountRecords.put(listType, newRecord);
        }
        
        // Also check if there's a corresponding delist record that's newer
        ListType delistType = listType.getDelistType();
        if (delistType != null) {
            EligibilityRecord delistRecord = accountRecords.get(delistType);
            if (delistRecord != null && delistRecord.getUploadTimestamp().isAfter(uploadTimestamp)) {
                // Delist is newer, keep the account eligible for this list type
                return;
            }
        }
    }
    
    /**
     * Process delist record (D_STR, D_CR, etc.)
     */
    private void processDelistRecord(String accountId, ListType delistType, LocalDateTime uploadTimestamp, String reason) {
        Map<ListType, EligibilityRecord> accountRecords = eligibilityData.get(accountId);
        
        // Add the delist record
        EligibilityRecord delistRecord = new EligibilityRecord(accountId, delistType, uploadTimestamp, reason);
        accountRecords.put(delistType, delistRecord);
        
        // Check if this delist supersedes any existing ineligibility record
        ListType ineligibilityType = delistType.getIneligibilityType();
        if (ineligibilityType != null) {
            EligibilityRecord ineligibilityRecord = accountRecords.get(ineligibilityType);
            if (ineligibilityRecord != null && uploadTimestamp.isAfter(ineligibilityRecord.getUploadTimestamp())) {
                // Delist is newer, the account becomes eligible for this type
                ineligibilityRecord.setActive(false);
            }
        }
    }
    
    /**
     * Check eligibility for an account
     */
    public EligibilityCheckResponse checkEligibility(String accountId) {
        EligibilityCheckResponse response = new EligibilityCheckResponse();
        response.setAccountId(accountId);
        
        Map<ListType, EligibilityRecord> accountRecords = eligibilityData.get(accountId);
        
        if (accountRecords == null || accountRecords.isEmpty()) {
            response.setEligible(true);
            response.setMessage("Account is eligible for loan - no records found");
            return response;
        }
        
        List<EligibilityCheckResponse.IneligibilityReason> ineligibilityReasons = new ArrayList<>();
        
        // Check each ineligibility list type
        for (ListType listType : Arrays.asList(ListType.STR, ListType.CR, ListType.MULTIPLE_ACCOUNT, ListType.FDM, ListType.SST)) {
            EligibilityRecord ineligibilityRecord = accountRecords.get(listType);
            EligibilityRecord delistRecord = accountRecords.get(listType.getDelistType());
            
            // Determine if account is ineligible for this list type
            if (isIneligibleForListType(ineligibilityRecord, delistRecord)) {
                ineligibilityReasons.add(new EligibilityCheckResponse.IneligibilityReason(
                    listType, 
                    ineligibilityRecord.getReason(), 
                    ineligibilityRecord.getUploadTimestamp()
                ));
            }
        }
        
        if (ineligibilityReasons.isEmpty()) {
            response.setEligible(true);
            response.setMessage("Account is eligible for loan");
        } else {
            response.setEligible(false);
            response.setMessage(String.format("Account is ineligible due to %d reason(s)", ineligibilityReasons.size()));
            response.setIneligibilityReasons(ineligibilityReasons);
        }
        
        return response;
    }
    
    /**
     * Determine if account is ineligible for a specific list type based on timestamps
     */
    private boolean isIneligibleForListType(EligibilityRecord ineligibilityRecord, EligibilityRecord delistRecord) {
        // No ineligibility record exists
        if (ineligibilityRecord == null) {
            return false;
        }
        
        // No delist record exists, so ineligible
        if (delistRecord == null) {
            return ineligibilityRecord.isActive();
        }
        
        // Compare timestamps - if delist is newer, account is eligible
        return ineligibilityRecord.getUploadTimestamp().isAfter(delistRecord.getUploadTimestamp()) 
               && ineligibilityRecord.isActive();
    }
    
    /**
     * Get all records for debugging/admin purposes
     */
    public Map<String, Map<ListType, EligibilityRecord>> getAllRecords() {
        return new HashMap<>(eligibilityData);
    }
    
    /**
     * Get records for a specific account
     */
    public Map<ListType, EligibilityRecord> getAccountRecords(String accountId) {
        return eligibilityData.getOrDefault(accountId, new HashMap<>());
    }
    
    /**
     * Get summary statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAccounts", eligibilityData.size());
        
        Map<ListType, Long> countByListType = new HashMap<>();
        for (ListType listType : ListType.values()) {
            long count = eligibilityData.values().stream()
                .mapToLong(accountRecords -> accountRecords.containsKey(listType) ? 1 : 0)
                .sum();
            countByListType.put(listType, count);
        }
        stats.put("recordsByListType", countByListType);
        
        return stats;
    }
    
    /**
     * Clear all data (for testing purposes)
     */
    public void clearAllData() {
        eligibilityData.clear();
    }
}
