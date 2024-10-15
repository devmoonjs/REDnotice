package io.rednotice.member.repository;

import io.rednotice.member.entity.Member;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUserAndWorkspace(User user, WorkSpace workSpace);

    @Query("SELECT m.workspace.id FROM Member m WHERE m.user = :user")
    List<Long> findWorkspaceByUser(@Param("user") User user);
}
