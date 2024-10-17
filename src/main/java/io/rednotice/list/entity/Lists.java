package io.rednotice.list.entity;

import io.rednotice.board.entity.Board;
import io.rednotice.card.entity.Card;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.workspace.entity.WorkSpace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Card> cardList = new ArrayList<>();

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
