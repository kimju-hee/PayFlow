package com.payflow.merchant.service;

import com.payflow.merchant.domain.UserAccount;
import com.payflow.merchant.dto.MeView;
import com.payflow.merchant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository users;

    @Transactional(readOnly = true)
    public MeView me(Authentication auth) {
        if (auth == null || auth.getName() == null) throw new IllegalStateException("unauthenticated");
        String email = auth.getName();
        UserAccount u = users.findByEmail(email).orElseThrow(() -> new IllegalStateException("user_not_found"));
        return new MeView(u.getId(), u.getEmail());
    }
}
