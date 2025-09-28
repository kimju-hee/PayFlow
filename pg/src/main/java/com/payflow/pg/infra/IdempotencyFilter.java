package com.payflow.pg.infra;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
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
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String key = req.getHeader("Idempotency-Key");
        if (StringUtils.hasText(key)) {
            Boolean ok = redis.opsForValue().setIfAbsent("idem:pg:"+key, "1", Duration.ofMinutes(10));
            if (Boolean.FALSE.equals(ok)) {
                res.setStatus(409);
                res.setContentType("application/json");
                res.getWriter().write("{\"success\":false,\"error\":\"duplicate_request\"}");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
