package io.rednotice.workspace.service;

import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.user.repository.UserRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import io.rednotice.workspace.request.ChangeMemberRoleRequest;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.response.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkSpaceAdminService {

    private final UserRepository userRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public WorkSpaceResponse saveWorkSpace(AuthUser authUser, WorkSpaceSaveRequest request) {

        User user = userRepository.findById(authUser.getId()).orElseThrow();
        WorkSpace workspace = workSpaceRepository.save(new WorkSpace(request, user));

        memberRepository.save(new Member(user, workspace, MemberRole.MANAGE));

        return WorkSpaceResponse.of(workspace);
    }

    @Transactional
    public void changeMemberRole(Long workSpaceId, Long memberId, ChangeMemberRoleRequest request) {

        Member member = memberRepository.getMember(memberId, workSpaceId);
        member.changeRole(request.getMemberRole());
    }
}
