package com.javatab.student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEvent {
    private String username;
    private String email;
}
