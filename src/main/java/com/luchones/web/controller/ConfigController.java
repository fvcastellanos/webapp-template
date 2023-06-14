package com.luchones.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/config")
public class ConfigController {

    @GetMapping
    @PreAuthorize("hasPermission(this, 'admin')")    
    public String index() {

        return "config/index";
    }
}
