package com.codewithadel.Mentor.dto;

import com.codewithadel.Mentor.model.Users;

public record UserResponseDto(Long id, String username) {
    public static UserResponseDto fromEntity(Users user) {
        return new UserResponseDto(user.getId(), user.getUsername());
    }
}
