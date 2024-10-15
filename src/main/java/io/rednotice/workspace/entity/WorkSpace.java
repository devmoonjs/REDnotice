package io.rednotice.workspace.entity;

import io.rednotice.board.entity.Board;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class WorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "workspace")
    private List<User> userList = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Board> boardList = new ArrayList<>();

    public WorkSpace(WorkSpaceSaveRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeDescription(String description) {
        this.description = description;
    }
}
