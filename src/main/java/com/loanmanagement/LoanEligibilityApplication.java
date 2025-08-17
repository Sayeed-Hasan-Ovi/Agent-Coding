package com.loanmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoanEligibilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanEligibilityApplication.class, args);
        System.out.println("\n" +
                "==============================================\n" +
                "🏦 Loan Eligibility Management System Started\n" +
                "==============================================\n" +
                "📊 Dashboard: http://localhost:8080\n" +
                "🔍 API Base: http://localhost:8080/api/loan-eligibility\n" +
                "❤️  Health Check: http://localhost:8080/api/loan-eligibility/health\n" +
                "==============================================\n"
        );
    }
}
