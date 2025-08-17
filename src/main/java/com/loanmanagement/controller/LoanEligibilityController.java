package com.loanmanagement.controller;

import com.loanmanagement.dto.EligibilityCheckRequest;
import com.loanmanagement.dto.EligibilityCheckResponse;
import com.loanmanagement.dto.ListUploadResponse;
import com.loanmanagement.model.EligibilityRecord;
import com.loanmanagement.model.ListType;
import com.loanmanagement.service.EligibilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/loan-eligibility")
@CrossOrigin(origins = "*")
public class LoanEligibilityController {
    
    @Autowired
    private EligibilityService eligibilityService;
    
    /**
     * Upload STR (Suspicious Activity) list
     */
    @PostMapping("/upload/str")
    public ResponseEntity<ListUploadResponse> uploadSTR(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.STR, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.STR, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.STR, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload CR (Control Report) list
     */
    @PostMapping("/upload/cr")
    public ResponseEntity<ListUploadResponse> uploadCR(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.CR, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.CR, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.CR, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload Multiple Account list
     */
    @PostMapping("/upload/multiple-account")
    public ResponseEntity<ListUploadResponse> uploadMultipleAccount(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.MULTIPLE_ACCOUNT, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.MULTIPLE_ACCOUNT, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.MULTIPLE_ACCOUNT, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload FDM (Fraudulent) list
     */
    @PostMapping("/upload/fdm")
    public ResponseEntity<ListUploadResponse> uploadFDM(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.FDM, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.FDM, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.FDM, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload SST (Special Support Recommendation) list
     */
    @PostMapping("/upload/sst")
    public ResponseEntity<ListUploadResponse> uploadSST(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.SST, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.SST, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.SST, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload D-STR (Delist STR) list
     */
    @PostMapping("/upload/d-str")
    public ResponseEntity<ListUploadResponse> uploadDelistSTR(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.D_STR, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.D_STR, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.D_STR, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload D-CR (Delist CR) list
     */
    @PostMapping("/upload/d-cr")
    public ResponseEntity<ListUploadResponse> uploadDelistCR(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.D_CR, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.D_CR, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.D_CR, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload D-Multiple Account (Delist Multiple Account) list
     */
    @PostMapping("/upload/d-multiple-account")
    public ResponseEntity<ListUploadResponse> uploadDelistMultipleAccount(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.D_MULTIPLE_ACCOUNT, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.D_MULTIPLE_ACCOUNT, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.D_MULTIPLE_ACCOUNT, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload D-FDM (Delist FDM) list
     */
    @PostMapping("/upload/d-fdm")
    public ResponseEntity<ListUploadResponse> uploadDelistFDM(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.D_FDM, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.D_FDM, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.D_FDM, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Upload D-SST (Delist SST) list
     */
    @PostMapping("/upload/d-sst")
    public ResponseEntity<ListUploadResponse> uploadDelistSST(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(
                new ListUploadResponse(ListType.D_SST, false, "File is empty")
            );
        }
        
        try {
            ListUploadResponse response = eligibilityService.uploadList(ListType.D_SST, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ListUploadResponse(ListType.D_SST, false, "Error: " + e.getMessage())
            );
        }
    }
    
    /**
     * Check eligibility for a specific account
     */
    @PostMapping("/check-eligibility")
    public ResponseEntity<EligibilityCheckResponse> checkEligibility(@Valid @RequestBody EligibilityCheckRequest request) {
        try {
            EligibilityCheckResponse response = eligibilityService.checkEligibility(request.getAccountId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            EligibilityCheckResponse errorResponse = new EligibilityCheckResponse();
            errorResponse.setAccountId(request.getAccountId());
            errorResponse.setEligible(false);
            errorResponse.setMessage("Error checking eligibility: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Check eligibility via GET (for simple URL-based access)
     */
    @GetMapping("/check-eligibility/{accountId}")
    public ResponseEntity<EligibilityCheckResponse> checkEligibilityGet(@PathVariable String accountId) {
        try {
            EligibilityCheckResponse response = eligibilityService.checkEligibility(accountId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            EligibilityCheckResponse errorResponse = new EligibilityCheckResponse();
            errorResponse.setAccountId(accountId);
            errorResponse.setEligible(false);
            errorResponse.setMessage("Error checking eligibility: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Get account records for debugging
     */
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Map<ListType, EligibilityRecord>> getAccountRecords(@PathVariable String accountId) {
        try {
            Map<ListType, EligibilityRecord> records = eligibilityService.getAccountRecords(accountId);
            return ResponseEntity.ok(records);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Get system statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        try {
            Map<String, Object> stats = eligibilityService.getStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Loan Eligibility Management System",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
