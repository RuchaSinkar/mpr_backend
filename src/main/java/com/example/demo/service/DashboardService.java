package com.example.demo.service;

import com.example.demo.dto.DashboardResponse;
import com.example.demo.dto.DashboardResponse.CompanyMatch;
import com.example.demo.model.StudentProfile;
import com.example.demo.repository.MatchResultRepository;
import com.example.demo.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final MatchResultRepository matchResultRepo;
    private final StudentProfileRepository profileRepo;

    public DashboardResponse getDashboard(Integer userId) {

        // 1. Get student_id from user_id
        StudentProfile profile = profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));

        Integer studentId = profile.getStudentId();

        // 2. Fetch all matched companies from DB
        List<Object[]> rows = matchResultRepo.findMatchesByStudentId(studentId);

        List<CompanyMatch> companies = new ArrayList<>();

        for (Object[] row : rows) {
            CompanyMatch c = new CompanyMatch();
            c.setMatchId(row[0] != null ? ((Number) row[0]).intValue() : null);
            // row[1] = student_id, skip
            c.setJobId(row[2] != null ? ((Number) row[2]).intValue() : null);
            c.setJobTitle((String) row[3]);
            c.setJobDomain((String) row[4]);
            c.setSalary((String) row[5]);
            c.setDeadline((String) row[6]);
            c.setCompanyName((String) row[7]);
            c.setIndustry((String) row[8]);
            c.setLocation((String) row[9]);
            c.setSkillMatchPercent(row[10] != null ? ((Number) row[10]).doubleValue() : 0.0);
            c.setExperienceMatchPercent(row[11] != null ? ((Number) row[11]).doubleValue() : 0.0);
            c.setProjectMatchPercent(row[12] != null ? ((Number) row[12]).doubleValue() : 0.0);
            c.setTotalMatchPercent(row[13] != null ? ((Number) row[13]).doubleValue() : 0.0);
            companies.add(c);
        }

        // Fetch required skills for all jobs in one query -> ADDED
        if (!companies.isEmpty()) {
            List<Integer> jobIds = companies.stream()
                    .map(CompanyMatch::getJobId)
                    .collect(Collectors.toList());

            List<Object[]> skillRows = matchResultRepo.findSkillsByJobIds(jobIds);

            // Map jobId → list of skill names
            Map<Integer, List<String>> skillMap = new HashMap<>();
            for (Object[] sr : skillRows) {
                Integer jobId = ((Number) sr[0]).intValue();
                String skillName = (String) sr[1];
                skillMap.computeIfAbsent(jobId, k -> new ArrayList<>()).add(skillName);
            }

            // Attach skills to each company
            for (CompanyMatch c : companies) {
                c.setRequiredSkills(skillMap.getOrDefault(c.getJobId(), new ArrayList<>()));
            }
        }//TILL THIS


        // 3. Compute overall average match
        double overallMatch = companies.stream()
                .mapToDouble(CompanyMatch::getTotalMatchPercent)
                .average()
                .orElse(0.0);

        // 4. Top package
        CompanyMatch topPackage = companies.stream()
                .max(Comparator.comparingDouble(c -> parseSalary(c.getSalary())))
                .orElse(null);

        // 5. Earliest deadline
        CompanyMatch earliest = companies.stream()
                .min(Comparator.comparing(CompanyMatch::getDeadline))
                .orElse(null);

        // 6. Build response
        DashboardResponse response = new DashboardResponse();
        response.setStudentId(studentId);
        response.setOverallMatchPercent(Math.round(overallMatch * 100.0) / 100.0);
        response.setTotalCompaniesMatched(companies.size());
        response.setTopPackage(topPackage);
        response.setEarliestDeadline(earliest);
        response.setCompanies(companies);

        return response;
    }

    // Parses "12 LPA" → 12.0 for comparison
    private double parseSalary(String salary) {
        if (salary == null) return 0;
        try {
            return Double.parseDouble(salary.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}