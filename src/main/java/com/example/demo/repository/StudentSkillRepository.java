package com.example.demo.repository;

import com.example.demo.model.StudentSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentSkillRepository  extends JpaRepository<StudentSkill, Integer> {
}
