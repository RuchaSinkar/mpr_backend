package com.example.demo.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class StudentExperienceId implements Serializable {
    private Integer studentId;
    private Integer experienceId;
}