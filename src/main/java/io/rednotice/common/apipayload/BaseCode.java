package io.rednotice.common.apipayload;

import io.rednotice.common.apipayload.dto.ReasonDto;
import org.springframework.http.HttpStatus;

public interface BaseCode {
    public ReasonDto getReasonHttpStatus();
}
