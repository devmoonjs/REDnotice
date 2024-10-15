package io.rednotice.common.apipayload.status;

import io.rednotice.common.apipayload.BaseCode;
import io.rednotice.common.apipayload.dto.ReasonDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseCode {

    // user
    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "404", "존재하지 않은 유저입니다"),
    _NOT_FOUND_(HttpStatus.NOT_FOUND, "404", "존재하지 않은 유저입니다"),

    // workspace
    _NOT_FOUND_WORKSPACE(HttpStatus.NOT_FOUND, "404", "존재하지 않은 워크스페이스입니다"),

    // member
    _READ_ONLY_ROLE(HttpStatus.FORBIDDEN, "403", "읽기 전용 권한으로 인해 카드 생성/수정/삭제가 불가능합니다"),

    // card
    _NOT_FOUND_CARD(HttpStatus.NOT_FOUND, "404", "존재하지 않는 카드입니다");

    private HttpStatus httpStatus;
    private String statusCode;
    private String message;


    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .statusCode(statusCode)
                .message(message)
                .httpStatus(httpStatus)
                .success(false)
                .build();
    }
}
