package com.anas.controller;

import com.anas.model.User;
import com.anas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        return userService.loginUser(email, password);
    }
}
