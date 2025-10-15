package com.codewithadel.Mentor;

import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.repository.UsersRepo;
import com.codewithadel.Mentor.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UsersRepo usersRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerUser_ShouldHashPasswordAndPersist_WhenUsernameIsAvailable() {
        UserRegistrationDto registrationDto = new UserRegistrationDto("alice", "plainSecret");
        Users persistedUser = new Users("alice", "hashed-secret");
        when(usersRepo.existsByUsername("alice")).thenReturn(false);
        when(passwordEncoder.encode("plainSecret")).thenReturn("hashed-secret");
        when(usersRepo.save(any(Users.class))).thenReturn(persistedUser);

        Users result = userService.registerUser(registrationDto);

        assertSame(persistedUser, result, "registerUser should return the saved entity instance");

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepo).save(userCaptor.capture());
        Users userToSave = userCaptor.getValue();
        assertEquals("alice", userToSave.getUsername());
        assertEquals("hashed-secret", userToSave.getPasswordHash());

        verify(usersRepo).existsByUsername("alice");
        verify(passwordEncoder).encode("plainSecret");
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameAlreadyExists() {
        UserRegistrationDto registrationDto = new UserRegistrationDto("existing", "password");
        when(usersRepo.existsByUsername("existing")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(registrationDto));

        assertEquals("Username already exists", exception.getMessage());
        verify(usersRepo, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }
    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        Users storedUser = new Users("bob", "hash");
        when(usersRepo.findByUsername("bob")).thenReturn(storedUser);

        Optional<Users> result = userService.findByUsername("bob");

        assertTrue(result.isPresent());
        assertSame(storedUser, result.orElseThrow());
        verify(usersRepo).findByUsername("bob");
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserDoesNotExist() {
        when(usersRepo.findByUsername("unknown")).thenReturn(null);

        Optional<Users> result = userService.findByUsername("unknown");

        assertTrue(result.isEmpty());
        verify(usersRepo).findByUsername("unknown");
    }
}