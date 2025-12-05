package com.anas.service;

import com.anas.repository.UserRepository;
import com.anas.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "❌ Username already taken!";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            return "❌ Passwords do not match!";
        }
        user.setConfirmPassword(null);
        userRepository.save(user);
        return "✅ User registered successfully!";
    }

    public Map<String, String> loginUser(String email, String password) {
        Map<String, String> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            response.put("status", "error");
            response.put("message", "❌ User not found!");
            return response;
        }

        User user = optionalUser.get();
        if (user.getPassword().equals(password)) {
            response.put("status", "success");
            response.put("message", "✅ Login successful!");
        } else {
            response.put("status", "error");
            response.put("message", "❌ Incorrect password!");
        }
        return response;
    }

}