package io.rednotice.user.enums;

import java.util.Arrays;

public enum UserRole {

    USER, ADMIN;

    public static UserRole of(String role) {
        if(role == null) {
            throw new NullPointerException("role is null");
        }
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 UserRole"));
    }
}
