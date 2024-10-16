package io.rednotice.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardDeleteRequest {
    private Long workSpaceId;
}
