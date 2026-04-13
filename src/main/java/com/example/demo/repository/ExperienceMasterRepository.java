package com.example.demo.repository;

import com.example.demo.model.ExperienceMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ExperienceMasterRepository extends JpaRepository<ExperienceMaster, Integer> {
    Optional<ExperienceMaster> findByExperienceName(String experienceName);
}