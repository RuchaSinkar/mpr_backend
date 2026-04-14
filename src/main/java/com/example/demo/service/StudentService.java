package com.example.demo.service;

import com.example.demo.dto.StudentRequest;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentProfileRepository profileRepo;
    private final StudentSkillRepository skillRepo;
    private final StudentExperienceRepository expRepo;
    private final SkillRepository skillLookupRepo;
    private final ExperienceMasterRepository experienceMasterRepo;
    private final AppUserRepository userRepo;

    public void saveStudent(StudentRequest request,Integer userId) {
        // 1. Create user in app_user first (temporary until auth is done)
        //no need now since sign up created
        // 2. Save student_profile
        StudentProfile profile = new StudentProfile();
        profile.setUserId(userId);
        profile.setCgpa(request.getCgpa());
        profile.setActiveBacklog(request.getActiveBacklogs());
        profile.setBranch(request.getBranch());
        profile.setDegree(request.getDegree());
        profile.setGraduationYear(Integer.parseInt(request.getGraduationYear()));
        profile.setDomainInterest(request.getDomain());
        profile.setTotalProjects(request.getProjects());
        profile.setProfileCompleted(true);
        // profile.setUserId(...)  ← set this once auth is done

        profile = profileRepo.save(profile);
        Integer studentId = profile.getStudentId();

        // 3. Save student_skill
        for (String skillName : request.getSkills()) {

            String normalizedSkill = skillName
                    .trim()
                    .toLowerCase()
                    .replaceAll("\\s+", " ");

            Skill skill = skillLookupRepo.findBySkillName(normalizedSkill)
                    .orElseThrow(() -> new RuntimeException("Skill not found: " + normalizedSkill));

            StudentSkill s = new StudentSkill();
            s.setStudentId(studentId);
            s.setSkillId(skill.getSkillId());
            skillRepo.save(s);
        }

        // 4. Save student_experience
        request.getExperience().forEach((expName, count) -> {
            ExperienceMaster master = experienceMasterRepo.findByExperienceName(expName)
                    .orElseThrow(() -> new RuntimeException("Experience not found: " + expName));

            StudentExperienceId expId = new StudentExperienceId();
            expId.setStudentId(studentId);
            expId.setExperienceId(master.getExperienceId());

            StudentExperience exp = new StudentExperience();
            exp.setId(expId);
            exp.setCount(count);
            expRepo.save(exp);
        });
    }
}