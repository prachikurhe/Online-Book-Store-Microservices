package com.codewithsachin.client.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @GetMapping("/user")
    public String userEndpoint() {
        return "Hello, User!";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello, Admin!";
    }
}
