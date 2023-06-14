package com.luchones.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    
    @GetMapping
    public String profile(Model model, @AuthenticationPrincipal OAuth2User principal) {

        model.addAttribute("profileName", principal.getName());

        return "profile/index";
    }
}
