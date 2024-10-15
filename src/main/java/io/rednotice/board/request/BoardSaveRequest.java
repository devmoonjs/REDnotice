package io.rednotice.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BoardSaveRequest {
    private final String title;
    private final String color;
    private Long workspaceId;
}
