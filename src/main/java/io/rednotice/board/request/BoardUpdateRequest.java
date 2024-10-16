package io.rednotice.board.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardUpdateRequest {
    private String title;
    private String color;
    private Long workSpaceId;
}
