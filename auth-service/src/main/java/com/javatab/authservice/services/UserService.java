package com.javatab.authservice.services;

import com.javatab.authservice.domain.Role;
import com.javatab.authservice.domain.User;
import com.javatab.authservice.repository.RoleRepository;
import com.javatab.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User createNewUser(User user) {
        Role role = roleRepository.findById("USER").get();
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setPassword(getEncodedPassword(user.getPassword()));
        return userRepository.save(user);
    }

    private String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
