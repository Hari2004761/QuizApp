package com.anas.service;

import com.anas.model.User;
import com.anas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository; // We mock the DB so we don't touch real data

    @InjectMocks
    private UserService userService; // We inject the mock into the real service

    // --- TEST REGISTRATION ---

    @Test
    void testRegisterUser_Success() {
        // 1. Setup
        User newUser = new User();
        newUser.setEmail("test@test.com");
        newUser.setUsername("testuser");
        newUser.setPassword("password123");
        newUser.setConfirmPassword("password123");

        // When the DB is checked, return empty (meaning user doesn't exist yet)
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());

        // 2. Execute
        Map<String, String> result = userService.registerUser(newUser);

        // 3. Verify
        assertEquals("success", result.get("status"));
        assertEquals("User registered successfully.", result.get("message"));
        verify(userRepository, times(1)).save(any(User.class)); // Make sure save was called
    }

    @Test
    void testRegisterUser_EmailExists() {
        User user = new User();
        user.setEmail("existing@test.com");

        // Simulate that email already exists in DB
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Map<String, String> result = userService.registerUser(user);

        assertEquals("error", result.get("status"));
        assertEquals("Email already exists.", result.get("message"));
        verify(userRepository, never()).save(any(User.class)); // Ensure we NEVER saved
    }

    @Test
    void testRegisterUser_PasswordsDoNotMatch() {
        User user = new User();
        user.setEmail("new@test.com");
        user.setUsername("newuser");
        user.setPassword("pass123");
        user.setConfirmPassword("WRONGPASS");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Map<String, String> result = userService.registerUser(user);

        assertEquals("error", result.get("status"));
        assertEquals("Passwords do not match.", result.get("message"));
    }

    // --- TEST LOGIN ---

    @Test
    void testLogin_Success() {
        String email = "valid@test.com";
        String password = "securepass";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);

        // Simulate finding the user
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Map<String, String> response = userService.loginUser(email, password);

        assertEquals("success", response.get("status"));
        assertEquals("Login successful.", response.get("message"));
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "ghost@test.com";

        // Simulate user NOT found
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Map<String, String> response = userService.loginUser(email, "anyPass");

        assertEquals("error", response.get("status"));
        assertEquals("User not found.", response.get("message"));
    }

    @Test
    void testLogin_WrongPassword() {
        String email = "valid@test.com";
        String correctPass = "realPass";
        String wrongPass = "fakePass";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(correctPass);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        // Try logging in with WRONG password
        Map<String, String> response = userService.loginUser(email, wrongPass);

        assertEquals("error", response.get("status"));
        assertEquals("Incorrect password.", response.get("message"));
    }
}
