package com.example.demo.controller;

import com.example.demo.dto.StudentRequest;
import com.example.demo.service.StudentService;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentController {
    private final StudentService studentService;
    private final JwtUtil jwtUtil;

    @PostMapping("/student")
    public ResponseEntity<?> save(@RequestBody StudentRequest request,
                                  @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer","");
        if(!jwtUtil.isValid(token)){
            return ResponseEntity.status(401).body("Invalid");
        }
        Integer userId= jwtUtil.extractUserId(token);
        studentService.saveStudent(request,userId);
        return ResponseEntity.ok("Saved Successfully");
    }
}
