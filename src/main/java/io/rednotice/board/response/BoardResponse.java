package io.rednotice.board.response;

import io.rednotice.board.entity.Board;
import io.rednotice.list.entity.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardResponse {
    private final String title;
    private final String color;
    private final List<Lists> lists;

    public static BoardResponse of(Board board) {
        return new BoardResponse(
                board.getTitle(),
                board.getColor(),
                board.getLists()
        );
    }
}
