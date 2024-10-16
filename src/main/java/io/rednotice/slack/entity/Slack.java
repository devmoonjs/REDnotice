package io.rednotice.slack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "slack_notify")
public class Slack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long userId;

    @Column(length = 1000)
    String message;

    LocalDateTime sentAt;

    public Slack(Long userId, String message, LocalDateTime sentAt) {
        this.userId = userId;
        this.message = message;
        this.sentAt = sentAt;
    }
}
