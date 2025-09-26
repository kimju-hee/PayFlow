package com.payflow.merchant.api;

import com.payflow.merchant.domain.User;
import com.payflow.merchant.dto.auth.*;
import com.payflow.merchant.repository.UserRepository;
import com.payflow.merchant.security.JwtUtil;
import com.payflow.merchant.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody @Valid SignupRequest req) {
        return ResponseEntity.ok(authService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest req,
                                               HttpServletResponse res) {
        Authentication auth = authService.login(req);
        String email = auth.getName();
        User u = userRepo.findByEmail(email).orElseThrow(); // 존재 보장됨(인증 성공)
        Long userId = u.getId();

        String accessToken = jwtUtil.generateAccessToken(userId, email);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        Cookie rt = new Cookie("refreshToken", refreshToken);
        rt.setHttpOnly(true);
        rt.setPath("/");
        rt.setMaxAge(60 * 60 * 24 * 14); // 14일
        res.addCookie(rt);

        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        Long userId = jwtUtil.parseRefreshToken(refreshToken);
        User u = userRepo.findById(userId).orElseThrow();
        String newAT = jwtUtil.generateAccessToken(userId, u.getEmail());
        return ResponseEntity.ok(new TokenResponse(newAT));
    }
}
