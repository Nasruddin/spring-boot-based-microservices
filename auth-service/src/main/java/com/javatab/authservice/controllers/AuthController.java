package com.javatab.authservice.controllers;

import com.javatab.authservice.domain.AuthRequest;
import com.javatab.authservice.domain.AuthResponse;
import com.javatab.authservice.domain.User;
import com.javatab.authservice.security.JwtUserDetailService;
import com.javatab.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUserDetailService userDetailService;
    private final UserService userService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        return userDetailService.createJwtToken(request);
    }

    @PostMapping("/register")
    public User registerNewUser(@RequestBody User user) {
        return userService.createNewUser(user);
    }
}
