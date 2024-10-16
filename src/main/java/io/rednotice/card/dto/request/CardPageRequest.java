package io.rednotice.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ParameterObject
@AllArgsConstructor
public class CardPageRequest {
    private Integer page;
    private Integer size;

    public int getPage() {
        return (page == null) ? 1 : page;  // page 값이 없을 경우 1으로 설정
    }

    public int getSize() {
        return (size == null) ? 10 : size;  // size 값이 없을 경우 10으로 설정
    }
}
