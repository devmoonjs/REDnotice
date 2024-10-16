package io.rednotice.attachment.entity;

import io.rednotice.card.entity.Card;
import io.rednotice.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Attachment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;

    private String fileName;

    private String fileType;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

}
