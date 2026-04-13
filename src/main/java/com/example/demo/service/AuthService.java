package com.example.demo.service;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepo;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        // Prevent duplicate email
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        AppUser user = new AppUser();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(request.getPassword()); // ⚠️ hash this later with BCrypt
        user.setRole("STUDENT");

        user = userRepo.save(user);

        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
        return new AuthResponse(token, user.getUserId(), user.getName(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        AppUser user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ⚠️ plain text check for now — use BCrypt later
        if (!user.getPasswordHash().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail());
        return new AuthResponse(token, user.getUserId(), user.getName(), user.getRole());
    }
}