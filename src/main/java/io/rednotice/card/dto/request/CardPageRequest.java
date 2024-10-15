package io.rednotice.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

// ParameterObject
@Getter
@AllArgsConstructor
public class CardPageRequest {
    private int page = 0;
    private int size = 10;
}
