package io.rednotice.workspace.enums;

import java.util.Arrays;

public enum MemberRole {

    READ,
    READ_WRITE;

    public static MemberRole of(String memberRole) {

        return Arrays.stream(MemberRole.values())
                .filter(t -> t.name().equalsIgnoreCase(memberRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다."));
    }
}
