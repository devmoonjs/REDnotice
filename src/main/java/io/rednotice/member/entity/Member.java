package io.rednotice.member.entity;

import io.rednotice.user.entity.User;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    public Member(User user, WorkSpace workSpace, MemberRole memberRole) {
        this.user = user;
        this.workSpace = workSpace;
        this.memberRole = memberRole;
    }

    public void changeRole(String memberRole) {
        this.memberRole = MemberRole.of(memberRole);
    }
}
