package com.example.demo.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class StudentRequest {
    private String fullName;
    private String email;
    private String phone;
    private Double cgpa;
    private String branch;
    private String degree;
    private String graduationYear;
    private String domain;
    private Integer projects;
    private String github;
    private List<String> skills;
    private Boolean activeBacklogs;
    private Map<String, Integer> experience;
}
