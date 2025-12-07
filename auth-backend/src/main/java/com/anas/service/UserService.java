package com.anas.service;

import com.anas.model.User;
import com.anas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Map<String, String> response(String status, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        return map;
    }

    public Map<String, String> registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return response("error", "Email already exists.");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return response("error", "Username already taken.");
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return response("error", "Passwords do not match.");
        }
        user.setConfirmPassword(null);
        userRepository.save(user);
        return response("success", "User registered successfully.");
    }

    public Map<String, String> loginUser(String email, String password) {
        Map<String, String> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            response.put("status", "error");
            response.put("message", "User not found.");
            return response;
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            response.put("status", "success");
            response.put("message", "Login successful.");
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("username", user.getUsername());
            response.put("country", user.getCountry());
            response.put("email", user.getEmail());
        } else {
            response.put("status", "error");
            response.put("message", "Incorrect password.");
        }
        return response;
    }
}
