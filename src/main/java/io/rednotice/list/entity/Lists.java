package io.rednotice.list.entity;

import io.rednotice.board.entity.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(length = 20)
    private String name;
    // Integer는 null이 되니까, int로 하자(이유 - 입력 없으면 null대신 0을 뱉는다.)
    private int sequence;

}
