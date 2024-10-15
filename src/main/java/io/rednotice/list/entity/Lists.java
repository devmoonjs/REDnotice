package io.rednotice.list.entity;

import io.rednotice.board.entity.Board;
import io.rednotice.list.request.ListsSaveRequest;
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
    @Column(unique = true)
    private int sequence;

    public Lists(ListsSaveRequest listsSaveRequest, Board board) {
        this.name = listsSaveRequest.getName();
        this.board = board;
        this.sequence = listsSaveRequest.getSequence();
    }

    public void changeName(String name){
        this.name = name;
    }

    public void changeSequence(int sequence){
        this.sequence = sequence;
    }
}
