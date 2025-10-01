package com.codewithadel.Mentor.controller;

import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        Users saved = service.register(user);
        return ResponseEntity.status(201).body(saved).getBody();

    }
}
