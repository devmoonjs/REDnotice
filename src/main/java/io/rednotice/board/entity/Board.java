package io.rednotice.board.entity;

import io.rednotice.list.entity.Lists;
import io.rednotice.workspace.entity.WorkSpace;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.PERSIST)
    private List<Lists> lists;


    @Column(length = 20)
    private String title;

    @Column(length = 20)
    private String color;



}