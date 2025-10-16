package com.codewithadel.Mentor.service;

import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.exception.InvalidRegistrationDataException;

import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.repository.UsersRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersRepo usersRepo, PasswordEncoder passwordEncoder) {
        this.usersRepo = usersRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users registerUser(UserRegistrationDto userData) {
        if (userData == null
                || userData.username() == null
                || userData.username().isBlank()
                || userData.password() == null
                || userData.password().isBlank()) {
            throw new InvalidRegistrationDataException("Username and password must not be empty");
        }

        if (usersRepo.existsByUsername(userData.username())) {
            throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(userData.password());
        Users user = new Users(userData.username(), hashedPassword);
        return usersRepo.save(user);
    }

    @Override
    public Optional<Users> findByUsername(String username) {
        return Optional.ofNullable(usersRepo.findByUsername(username));
    }

    @Override
    public Users authenticate(String username, String rawPassword) {
        Users user = usersRepo.findByUsername(username);
        if (user == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }
        return user;
    }
}
