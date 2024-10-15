package io.rednotice.board.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardUpdateRequest {
    @Nullable
    private String title;

    @Nullable
    private String color;
}
