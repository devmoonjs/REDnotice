package io.rednotice.auth.response;

import io.rednotice.user.enums.UserRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponse {

    private final Long id;
    private final String email;
    private final String name;
    private final LocalDateTime createdAt;

    public SignupResponse(Long id, String email, String name, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
    }
}
