package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class StudentProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer studentId;

    private Integer userId;

    private Double cgpa;

    private Boolean activeBacklog;

    private String branch;

    private String degree;

    private Integer graduationYear;

    private String domainInterest;

    private Integer totalProjects;

    private Boolean profileCompleted = false;
}
