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
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // --- TEST REGISTRATION ---

    @Test
    void testRegisterUser_Success() {
        User newUser = new User();
        newUser.setEmail("test@test.com");
        newUser.setUsername("testuser");
        newUser.setPassword("password123");
        newUser.setConfirmPassword("password123");

        // Simulate that user does NOT exist yet
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByUsername(newUser.getUsername())).thenReturn(Optional.empty());

        String result = userService.registerUser(newUser);

        // Note: I copied the specific emoji string from your code to match exactly
        assertEquals("Success: User registered successfully!", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_EmailExists() {
        User user = new User();
        user.setEmail("existing@test.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        String result = userService.registerUser(user);

        assertEquals("Email already exists", result);
        verify(userRepository, never()).save(any(User.class));
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

        String result = userService.registerUser(user);

        assertEquals("Error: Passwords do not match!", result);
    }

    // --- TEST LOGIN ---

    @Test
    void testLogin_Success() {
        String email = "valid@test.com";
        String password = "securepass";

        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);
        mockUser.setFirstName("John"); // Added these because your new login returns them
        mockUser.setLastName("Doe");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

        Map<String, String> response = userService.loginUser(email, password);

        assertEquals("success", response.get("status"));
        assertEquals("Success: Login successful!", response.get("message"));
        assertEquals("John", response.get("firstName")); // Verified extra data
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "ghost@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Map<String, String> response = userService.loginUser(email, "anyPass");

        assertEquals("error", response.get("status"));
        assertEquals("Error: User not found!", response.get("message"));
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

        Map<String, String> response = userService.loginUser(email, wrongPass);

        assertEquals("error", response.get("status"));
        assertEquals("Error: Incorrect password!", response.get("message"));
    }
}