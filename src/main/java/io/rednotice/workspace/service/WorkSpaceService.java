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
import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceNameResponse;
import io.rednotice.workspace.response.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkSpaceService {

    private final WorkSpaceRepository workSpaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public List<WorkSpaceNameResponse> findWorkSpaces(AuthUser authUser) {

        User user = userRepository.getUserById(authUser.getId());

        List<Long> workSpaceIdList = memberRepository.findWorkspaceByUser(user);
        List<String> workSpaceList = workSpaceRepository.findByIdList(workSpaceIdList);

        return workSpaceList.stream()
                .map(WorkSpaceNameResponse::of)
                .collect(Collectors.toList());
    }

    public WorkSpaceResponse findWorkspaceById(AuthUser authUser, Long id) {

        isMember(authUser.getId(), id);

        return WorkSpaceResponse.of(findWorkSpaceById(id));
    }

    @Transactional
    public WorkSpaceResponse updateWorkSpace(AuthUser authUser, Long id, WorkSpaceUpdateRequest request) {

        Member member = isMember(authUser.getId(), id);
        isManage(member);

        WorkSpace workSpace = findWorkSpaceById(id);

        workSpace.updateWorkspace(request);

        return WorkSpaceResponse.of(workSpace);
    }

    @Transactional
    public void deleteWorkSpace(AuthUser authUser, Long id) {

        Member member = isMember(authUser.getId(), id);
        isManage(member);

        workSpaceRepository.deleteById(id);
    }

    @Transactional
    public void addMember(AuthUser authUser, Long id, AddMemberRequest request) {

        Member member = isMember(authUser.getId(), id);
        isManage(member);

        User newUser = userRepository.getUserByEmail(request.getEmail());
        WorkSpace workSpace = findWorkSpaceById(id);

        if (memberRepository.findByUserAndWorkspace(newUser, workSpace).isPresent()) {
            throw new ApiException(ErrorStatus._DUPLICATE_MANAGE);
        }

        memberRepository.save(new Member(newUser, workSpace, MemberRole.of(request.getMemberRole())));
    }

    private Member isMember(Long userId, Long workspaceId) {
        return memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId).orElseThrow(
                () -> new ApiException(ErrorStatus._INVALID_REQUEST)
        );
    }

    private WorkSpace findWorkSpaceById(Long id) {
        return workSpaceRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE)
        );
    }

    public void isManage(Member member) {
        if (member.getMemberRole() != MemberRole.MANAGE) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }
}
