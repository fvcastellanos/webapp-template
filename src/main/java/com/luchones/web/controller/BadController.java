package com.luchones.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/errors")
public class BadController {
    
    @GetMapping
    public String index() throws Exception {

        throw new Exception("Test Exception for Controller Advice");
    }

    @GetMapping("/access")
    @PreAuthorize("hasPermission(this, 'foo')")
    public String denied() {

        return "index";
    }
}
