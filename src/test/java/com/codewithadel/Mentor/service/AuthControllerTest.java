package com.codewithadel.Mentor.service;

import com.codewithadel.Mentor.controller.AuthController;
import com.codewithadel.Mentor.dto.LoginRequestDto;
import com.codewithadel.Mentor.dto.UserRegistrationDto;
import com.codewithadel.Mentor.model.Users;
import com.codewithadel.Mentor.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("POST /api/auth/register")
    class Register {
        @Test
        @DisplayName("returns 201 CREATED when registration succeeds")
        void registerSuccess() throws Exception {
            Users savedUser = withId(new Users("newUser", "hash"), 1L);
            when(userService.registerUser(any(UserRegistrationDto.class))).thenReturn(savedUser);

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserRegistrationDto("newUser", "pass"))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.username", is("newUser")));
        }

        @Test
        @DisplayName("returns 409 CONFLICT when username already exists")
        void registerConflict() throws Exception {
            when(userService.registerUser(any(UserRegistrationDto.class)))
                    .thenThrow(new IllegalArgumentException("Username already exists"));

            mockMvc.perform(post("/api/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserRegistrationDto("existing", "pass"))))
                    .andExpect(status().isConflict())
                    .andExpect(status().reason("Username already exists"));
        }
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("returns 200 OK when authentication succeeds")
        void loginSuccess() throws Exception {
            Users authenticatedUser = withId(new Users("john", "hash"), 2L);
            when(userService.authenticate(eq("john"), eq("password"))).thenReturn(authenticatedUser);

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequestDto("john", "password"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(2)))
                    .andExpect(jsonPath("$.username", is("john")));
        }

        @Test
        @DisplayName("returns 401 UNAUTHORIZED when credentials are incorrect")
        void loginIncorrectPassword() throws Exception {
            when(userService.authenticate(eq("john"), eq("wrong")))
                    .thenThrow(new IllegalArgumentException("Invalid username or password"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequestDto("john", "wrong"))))
                    .andExpect(status().isUnauthorized())
                    .andExpect(status().reason("Invalid username or password"));
        }

        @Test
        @DisplayName("returns 401 UNAUTHORIZED when the username does not exist")
        void loginUnknownUser() throws Exception {
            when(userService.authenticate(eq("unknown"), eq("password")))
                    .thenThrow(new IllegalArgumentException("Invalid username or password"));

            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new LoginRequestDto("unknown", "password"))))
                    .andExpect(status().isUnauthorized())
                    .andExpect(status().reason("Invalid username or password"));
        }
    }

    private static Users withId(Users user, long id) throws Exception {
        Field idField = Users.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
        return user;
    }
}
