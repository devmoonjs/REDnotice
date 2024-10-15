package io.rednotice.card.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardUpdateRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
    private Long workSpaceId;   // 소속 workSpace
}
