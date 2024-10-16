package io.rednotice.comment.entity;

import io.rednotice.card.entity.Card;
import io.rednotice.common.Timestamped;
import io.rednotice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment(String content, Card card, User user) {
        this.content = content;
        this.card = card;
        this.user = user;
    }

    public void changeContent(String content) {
        this.content = content;
    }
}
