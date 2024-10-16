package io.rednotice.member.service;

import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    public Member getMember(Long memberId, Long workspaceId) {
        return memberRepository.findMemberByUserIdAndWorkspaceId(memberId, workspaceId);
    }

    public List<Long> getWorkspaceList(User user) {
        return memberRepository.findWorkspaceByUser(user);
    }

    public Member validateMember(Long userId, Long workspaceId) {
        return memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_)
        );
    }

    public void checkManage(Member member) {
        if (member.getMemberRole() != MemberRole.MANAGE) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }

    public void checkReadAndWrite(Long userId, Long workSpaceId) {
        Member member = getMember(userId, workSpaceId);
        if (member.getMemberRole().equals(MemberRole.READ)) {
            throw new ApiException(ErrorStatus._READ_ONLY_ROLE);
        }
    }

    public void checkDuplicateMember(User user, WorkSpace workSpace) {
        memberRepository.findByUserAndWorkspace(user, workSpace).orElseThrow(
                () -> new ApiException(ErrorStatus._DUPLICATE_MANAGE)
        );
    }

}
