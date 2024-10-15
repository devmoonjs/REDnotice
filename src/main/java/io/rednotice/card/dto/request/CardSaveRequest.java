package io.rednotice.card.dto.request;

import io.rednotice.card.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardSaveRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private int seq;
    private Long workSpaceId;   // 소속 workSpace
    private Long boardId;   // 소속 board
    private Long listId;    // 소속 list

}
