package io.javatab.microservices.composite.course.config;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protectedapi")
public class ResourceController {

    @GetMapping("/public")
    public String publicResource() {
        return "This is a public resource. No authentication required.";
    }

    @GetMapping("/user")
    //@PreAuthorize("hasRole('USER')")
    public String userResource(Authentication authentication) {
        return "Hello, " + authentication.getName() + "! You have USER access.";
    }

    @GetMapping("/admin")
    //@PreAuthorize("hasRole('ADMIN')")
    public String adminResource(Authentication authentication) {
        return "Hello, " + authentication.getName() + "! You have ADMIN access.";
    }
}