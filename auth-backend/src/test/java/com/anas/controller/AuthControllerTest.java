package com.anas.controller;

import com.anas.model.User;
import com.anas.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser // Simulates a logged-in user so Security doesn't block us
    void testSignup_Success() throws Exception {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("new@test.com");
        user.setPassword("pass123");

        // Mock the service to say "Success"
        when(userService.registerUser(any(User.class))).thenReturn("✅ User registered successfully!");

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf()) // Needed for security
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("✅ User registered successfully!"));
    }

    @Test
    @WithMockUser
    void testSignup_Fail_EmailExists() throws Exception {
        User user = new User();
        user.setEmail("existing@test.com");

        when(userService.registerUser(any(User.class))).thenReturn("Email already exists");

        mockMvc.perform(post("/api/auth/signup")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("Email already exists"));
    }
}