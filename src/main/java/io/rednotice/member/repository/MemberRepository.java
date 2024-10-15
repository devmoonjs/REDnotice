package io.rednotice.member.repository;

import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.entity.Member;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.entity.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserAndWorkSpace(User user, WorkSpace workSpace);
    Optional<Member> findByUserIdAndWorkSpaceId(Long userId, Long workSpaceId);

    default Member getMember(Long userId, Long workSpaceId) {
        return findByUserIdAndWorkSpaceId(userId, workSpaceId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE));
    }
}
