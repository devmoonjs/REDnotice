package io.rednotice.card.dto.response;

import io.rednotice.card.entity.Card;
import io.rednotice.user.dto.UserDto;
import io.rednotice.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CardDetailResponse {   // 카드 단건 조회 시 반환 DTO
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final int seq;
    private final UserDto userDto;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
    // 댓글 구현완료되면 댓글 Dto 추가(댓글 작성자, 작성 내용, 날짜 ...)
    // 이때 batchsize 얼마나 할 것인지 확정하기

    public static CardDetailResponse of(Card card) {
        return new CardDetailResponse(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getDueDate(),
                card.getSeq(),
                new UserDto(card.getUser().getId(), card.getUser().getEmail(), card.getUser().getUsername()),
                card.getCreatedAt(),
                card.getModifiedAt()
                // 댓글 dto 추가
        );
    }
}
