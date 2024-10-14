package io.rednotice.user.enums;

import java.util.Arrays;

public enum UserRole {

    USER, ADMIN;

    public static UserRole of(String userRole) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(userRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다"));
    }
}
