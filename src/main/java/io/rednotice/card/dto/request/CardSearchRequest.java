package io.rednotice.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

// ParameterObject
@Getter
@AllArgsConstructor
public class CardSearchRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String managerName;
    private Long boardId;
}
