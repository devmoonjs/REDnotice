package io.rednotice.common.exception;

import io.rednotice.common.apipayload.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final BaseCode errorCode;
}
