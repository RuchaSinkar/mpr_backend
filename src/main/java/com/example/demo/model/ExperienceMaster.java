package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "experience_master")
@Data
public class ExperienceMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer experienceId;

    private String experienceName;

    private Integer points;
}