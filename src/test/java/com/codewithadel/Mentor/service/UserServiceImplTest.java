package com.codewithadel.Mentor.service;


import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.repository.UsersRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("registerUser")
    class RegisterUser {

        @Test
        @DisplayName("saves a new user when the username is available")
        void registerUserSuccess() {
            UserRegistrationDto registrationDto = new UserRegistrationDto("newUser", "plainPassword");
            when(usersRepo.existsByUsername("newUser")).thenReturn(false);
            when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
            Users persistedUser = new Users("newUser", "encodedPassword");
            when(usersRepo.save(any(Users.class))).thenReturn(persistedUser);

            Users result = userService.registerUser(registrationDto);

            assertThat(result).isSameAs(persistedUser);

            ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
            verify(usersRepo).save(userCaptor.capture());
            Users savedUser = userCaptor.getValue();
            assertThat(savedUser.getUsername()).isEqualTo("newUser");
            assertThat(savedUser.getPasswordHash()).isEqualTo("encodedPassword");
            verify(passwordEncoder).encode("plainPassword");
        }

        @Test
        @DisplayName("throws an exception when the username already exists")
        void registerUserExistingUsername() {
            UserRegistrationDto registrationDto = new UserRegistrationDto("existingUser", "password");
            when(usersRepo.existsByUsername("existingUser")).thenReturn(true);

            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> userService.registerUser(registrationDto));

            assertThat(exception.getMessage()).isEqualTo("Username already exists");
            verify(usersRepo, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
        }
    }

    @Nested
    @DisplayName("findByUsername")
    class FindByUsername {

        @Test
        @DisplayName("returns an Optional containing the user when found")
        void findExistingUser() {
            Users user = new Users("existing", "hash");
            when(usersRepo.findByUsername("existing")).thenReturn(user);

            Optional<Users> result = userService.findByUsername("existing");

            assertThat(result).isPresent().contains(user);
        }

        @Test
        @DisplayName("returns an empty Optional when the user is not found")
        void findMissingUser() {
            when(usersRepo.findByUsername("missing")).thenReturn(null);

            Optional<Users> result = userService.findByUsername("missing");

            assertThat(result).isEmpty();
        }
    }
}