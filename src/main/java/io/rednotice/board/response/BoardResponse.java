package io.rednotice.board.response;

import io.rednotice.board.entity.Board;
import io.rednotice.list.response.ListsResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardResponse {
    private final String title;
    private final String color;
    private final List<ListsResponse> lists;

    public static BoardResponse of(Board board) {
        return new BoardResponse(
                board.getTitle(),
                board.getColor(),
                board.getLists().stream().map(ListsResponse::of).toList()
        );
    }
}
