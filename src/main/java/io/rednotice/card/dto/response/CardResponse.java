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
public class CardResponse {
    private final Long id;
    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private final int seq;
    private final int views;
    private final UserDto userDto;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CardResponse of(Card card, User user) {
        return new CardResponse(
                card.getId(),
                card.getTitle(),
                card.getDescription(),
                card.getDueDate(),
                card.getSeq(),
                card.getViews(),
                new UserDto(user.getId(), user.getEmail(), user.getUsername()),
                card.getCreatedAt(),
                card.getModifiedAt()
        );
    }
}
