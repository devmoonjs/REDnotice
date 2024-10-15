package io.rednotice.card.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CardSearchDto {   // 카드 검색 시 반환 DTO
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final Long boardId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    @QueryProjection
    public CardSearchDto(Long id, String title, String description, LocalDate dueDate, Long boardId, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.boardId = boardId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
