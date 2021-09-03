package com.javatab.authservice.controllers;

import com.javatab.authservice.domain.Role;
import com.javatab.authservice.domain.User;
import com.javatab.authservice.repository.UserRepository;
import com.javatab.authservice.domain.AuthRequest;
import com.javatab.authservice.domain.AuthResponse;
import com.javatab.authservice.security.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/login")
public class AuthController {

    private AuthService authService;
    private UserRepository userRepository;

    @Autowired
    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        Role role1 = new Role();
        role1.setRoleName("USER");
        Role role2 = new Role();
        role2.setRoleName("ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(role1);
        roles.add(role2);
        user.setRoles(roles);
        User user1 = userRepository.save(user);
        return authService.login(request);
    }
}
