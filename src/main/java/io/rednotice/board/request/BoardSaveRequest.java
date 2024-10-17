package io.rednotice.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class BoardSaveRequest {
    private final String title;
    private Long workspaceId;
    private final String color;
}
