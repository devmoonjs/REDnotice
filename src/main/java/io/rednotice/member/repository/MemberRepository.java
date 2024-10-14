package io.rednotice.member.repository;

import io.rednotice.member.entity.Member;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserAndWorkSpace(User user, WorkSpace workSpace);
}
