package com.codewithsachin.discovery.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/info")
    public String home() {
        return "Auth Service is up!";
    }
}

