package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardResponse {
    private Integer studentId;
    private Double overallMatchPercent;
    private Integer totalCompaniesMatched;
    private CompanyMatch topPackage;
    private CompanyMatch earliestDeadline;
    private List<CompanyMatch> companies;

    @Data
    public static class CompanyMatch {
        private Integer matchId;
        private Integer jobId;
        private String jobTitle;
        private String jobDomain;
        private String companyName;
        private String industry;
        private String location;
        private String salary;
        private String deadline;
        private Double skillMatchPercent;
        private Double experienceMatchPercent;
        private Double projectMatchPercent;
        private Double totalMatchPercent;
        private List<String> requiredSkills;//Added update
    }
}