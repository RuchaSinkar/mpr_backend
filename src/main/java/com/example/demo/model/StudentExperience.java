package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "student_experience")
@Data
public class StudentExperience {

    @EmbeddedId
    private StudentExperienceId id;

    @Column(name = "count")
    private Integer count;
}