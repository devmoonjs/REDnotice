package io.rednotice.card.entity;

import io.rednotice.board.entity.Board;
import io.rednotice.common.Timestamped;
import io.rednotice.list.entity.Lists;
import io.rednotice.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "card")
@DynamicInsert
@NoArgsConstructor
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(nullable = false)
    private int seq;

    @ColumnDefault("0")
    private int views;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private Lists list;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "manager_id", nullable = false)
    private User manager;

    public Card(String title, String description, LocalDate dueDate, int seq, Board board, Lists list, User user) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.seq = seq;
        this.board = board;
        this.list = list;
        this.user = user;
        this.manager = user;
    }

    public void updateCard(String title, String description, LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public void changeSeq(int seq) {
        this.seq = seq;
    }

    public void updateViews(int views){
        this.views = views;
    }

    public void changeManager(User user) {
        this.manager = user;
    }
}
