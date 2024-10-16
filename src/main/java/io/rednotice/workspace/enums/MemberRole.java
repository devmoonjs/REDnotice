package io.rednotice.workspace.enums;

import java.util.Arrays;

/*
    READ 권한 : 읽기 전용으로 생성, 수정, 삭제가 불가능하며 조회만 가능.
    READ_WRITE 권한 : 워크스페이스 관련 기능을 제외한 나머지 기능 모두 사용 가능.
    MANAGE 권한 : 워크스페이스 생성을 제외한 모든 기능 사용 가능.
 */

public enum MemberRole {

    READ,
    READ_WRITE,
    MANAGE;

    public static MemberRole of(String memberRole) {

        return Arrays.stream(MemberRole.values())
                .filter(t -> t.name().equalsIgnoreCase(memberRole))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다."));
    }
}
