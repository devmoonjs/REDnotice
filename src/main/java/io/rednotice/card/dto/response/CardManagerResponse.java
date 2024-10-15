package io.rednotice.card.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CardManagerResponse {
    private final Long cardId;
    private final Long managerUserId;
}
