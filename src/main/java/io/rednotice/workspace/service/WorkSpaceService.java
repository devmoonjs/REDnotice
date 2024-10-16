package io.rednotice.workspace.service;

import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.entity.Member;
import io.rednotice.member.service.MemberService;
import io.rednotice.user.entity.User;
import io.rednotice.user.service.UserService;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceNameResponse;
import io.rednotice.workspace.response.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
    private final UserService userService;
    private final MemberService memberService;
    private final CharacterEncodingFilter characterEncodingFilter;

    public List<WorkSpaceNameResponse> findWorkSpaces(AuthUser authUser) {
        User user = userService.getUser(authUser.getId());

        List<Long> workSpaceIdList = memberService.getWorkspaceList(user);
        List<String> workSpaceList = workSpaceRepository.findByIdList(workSpaceIdList);

        return workSpaceList.stream()
                .map(WorkSpaceNameResponse::of)
                .collect(Collectors.toList());
    }

    public WorkSpaceResponse findWorkspace(AuthUser authUser, Long id) {
        memberService.validateMember(authUser.getId(), id);

        return WorkSpaceResponse.of(getWorkSpace(id));
    }

    @Transactional
    public WorkSpaceResponse updateWorkSpace(AuthUser authUser, Long id, WorkSpaceUpdateRequest request) {
        Member member = memberService.validateMember(authUser.getId(), id);
        memberService.checkManage(member);

        WorkSpace workSpace = getWorkSpace(id);
        workSpace.updateWorkspace(request);

        return WorkSpaceResponse.of(workSpace);
    }

    @Transactional
    public void deleteWorkSpace(AuthUser authUser, Long id) {
        Member member = memberService.getMember(authUser.getId(), id);
        memberService.checkManage(member);

        workSpaceRepository.deleteById(id);
    }

    @Transactional
    public void addMember(AuthUser authUser, Long id, AddMemberRequest request) {
        Member member = memberService.getMember(authUser.getId(), id);
        memberService.checkManage(member);

        User newUser = userService.getUserFromEmail(request.getEmail());
        WorkSpace workSpace = getWorkSpace(id);
        memberService.checkDuplicateMember(newUser, workSpace);

        memberService.saveMember(new Member(newUser, workSpace, MemberRole.of(request.getMemberRole())));
    }

    private WorkSpace getWorkSpace(Long id) {
        return workSpaceRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE)
        );
    }
}
