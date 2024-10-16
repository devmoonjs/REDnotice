package io.rednotice.workspace.entity;

import io.rednotice.board.entity.Board;
import io.rednotice.common.Timestamped;
import io.rednotice.member.entity.Member;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class WorkSpace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "workspace")
    private List<Board> boardList = new ArrayList<>();

    public WorkSpace(WorkSpaceSaveRequest request, User user) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.user = user;
    }

    public void updateWorkspace(WorkSpaceUpdateRequest request) {
        if (request.getName() != null) {
            this.name = request.getName();
        }

        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
    }
}
