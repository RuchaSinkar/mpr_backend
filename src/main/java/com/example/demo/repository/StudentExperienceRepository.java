package com.example.demo.repository;

import com.example.demo.model.StudentExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentExperienceRepository  extends JpaRepository<StudentExperience, Integer> {}
