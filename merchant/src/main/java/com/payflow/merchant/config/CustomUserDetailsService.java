package com.payflow.merchant.config;

import com.payflow.merchant.domain.UserAccount;
import com.payflow.merchant.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount u = userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("USER_NOT_FOUND"));
        String[] roles = u.getRoles() == null ? new String[]{"USER"} : u.getRoles().split(",");
        return User.withUsername(u.getEmail())
                .password(u.getPassword())
                .roles(roles)
                .build();
    }
}
