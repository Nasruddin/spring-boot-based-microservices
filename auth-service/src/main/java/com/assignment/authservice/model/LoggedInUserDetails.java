package com.assignment.authservice.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class LoggedInUserDetails {
    private final String username;
    private final String token;
}
