package com.codewithadel.Mentor.controller;

import com.codewithadel.Mentor.dto.LoginRequestDto;
import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.dto.UserResponseDto;
import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            Users savedUser = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.fromEntity(savedUser));

        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            Users user = userService.authenticate(loginRequestDto.username(), loginRequestDto.password());
            return ResponseEntity.ok(UserResponseDto.fromEntity(user));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ex.getMessage());
        }
    }
}