package com.javatab.authservice.controllers;

import com.javatab.authservice.domain.AuthRequest;
import com.javatab.authservice.domain.AuthResponse;
import com.javatab.authservice.domain.User;
import com.javatab.authservice.domain.UserEvent;
import com.javatab.authservice.security.JwtUserDetailService;
import com.javatab.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUserDetailService userDetailService;
    private final UserService userService;
    private final StreamBridge streamBridge;

    static final String USER_CREATED_OUTPUT = "userCreatedOutput";

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) throws Exception {
        return userDetailService.createJwtToken(request);
    }

    @PostMapping("/register")
    public User registerNewUser(@RequestBody User user) {
        User newUser = userService.createNewUser(user);
        UserEvent userEvent = UserEvent.builder().username(newUser.getUsername()).email(newUser.getEmail()).build();
        boolean sent = streamBridge.send(USER_CREATED_OUTPUT, userEvent);
        System.out.println("Message sent " + sent);
        return newUser;
    }
}
