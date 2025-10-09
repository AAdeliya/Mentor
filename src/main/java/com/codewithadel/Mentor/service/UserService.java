package com.codewithadel.Mentor.service;
import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.model.Users;
import java.util.Optional;

public interface UserService {
 Users registerUser(UserRegistrationDto userData);
Optional<Users> findByUsername(String username);

Users authenticate(String username, String rawPassword);
}
