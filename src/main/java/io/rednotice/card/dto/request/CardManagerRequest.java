package io.rednotice.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardManagerRequest {
    private Long managerId;
    private Long workSpaceId;   // 소속 workSpace
}
