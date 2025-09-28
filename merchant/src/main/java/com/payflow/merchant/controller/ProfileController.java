package com.payflow.merchant.controller;

import com.payflow.merchant.dto.MeView;
import com.payflow.merchant.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api")
public class ProfileController {
    private final ProfileService profiles;

    @GetMapping("/me")
    public MeView me(Authentication auth) { return profiles.me(auth); }
}
