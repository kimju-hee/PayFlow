package com.payflow.merchant.service;

import com.payflow.merchant.domain.UserAccount;            // ✅ 변경
import com.payflow.merchant.dto.auth.*;
import com.payflow.merchant.repository.UserRepository;
import com.payflow.merchant.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest req) {
        if (userRepo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("EMAIL_ALREADY_EXISTS");
        }
        UserAccount u = UserAccount.builder()                // ✅ UserAccount 사용
                .email(req.email())
                .password(encoder.encode(req.password()))
                .roles("USER")
                .build();
        userRepo.save(u);
        return new SignupResponse(u.getId(), u.getEmail());
    }

    public Authentication login(LoginRequest req) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );
    }

    public String generateAccessToken(Long userId, String email) {
        return jwtUtil.generateAccessToken(userId, email);
    }

    public String generateRefreshToken(Long userId) {
        return jwtUtil.generateRefreshToken(userId);
    }
}
