package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_result")
@Data
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer matchId;

    private Integer studentId;
    private Integer jobId;

    private Double skillMatchPercent;
    private Double experienceMatchPercent;
    private Double projectMatchPercent;
    private Double totalMatchPercent;

    private Boolean isEligible;
    private LocalDateTime calculatedAt;
}