package com.javatab.authservice.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEvent {
    private String username;
    private String email;
}
