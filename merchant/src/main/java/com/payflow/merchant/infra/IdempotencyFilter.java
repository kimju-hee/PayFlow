package com.payflow.merchant.infra;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class IdempotencyFilter extends OncePerRequestFilter {
    private final StringRedisTemplate redis;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String key = request.getHeader("Idempotency-Key");
        if (StringUtils.hasText(key)) {
            Boolean ok = redis.opsForValue().setIfAbsent("idem:merchant:" + key, "1", Duration.ofMinutes(10));
            if (Boolean.FALSE.equals(ok)) {
                response.setStatus(409);
                response.setContentType("application/json");
                response.getWriter().write("{\"success\":false,\"error\":\"duplicate_request\"}");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
