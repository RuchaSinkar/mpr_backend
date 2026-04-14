package com.example.demo.repository;

import com.example.demo.model.MatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchResultRepository extends JpaRepository<MatchResult, Integer> {

    @Query(value = """
        SELECT\s
            mr.match_id,
            mr.student_id,
            jr.job_id,
            jr.job_title,
            jr.job_domain,
            jr.salary,
            CAST(jr.deadline AS TEXT) AS deadline,
            cl.company_name,
            cl.industry,
            cl.location,
            mr.skill_match_percent,
            mr.experience_match_percent,
            mr.project_match_percent,
            mr.total_match_percent
        FROM match_result mr
        JOIN job_role jr ON mr.job_id = jr.job_id
        JOIN company_list cl ON jr.company_id = cl.company_id
        WHERE mr.student_id = :studentId
          AND mr.is_eligible = TRUE
        ORDER BY mr.total_match_percent DESC
   \s""", nativeQuery = true)
    List<Object[]> findMatchesByStudentId(@Param("studentId") Integer studentId);
}