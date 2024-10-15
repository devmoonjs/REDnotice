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

        User user = findUserByAuthUser(authUser);

        List<Long> workSpaceIdList = memberRepository.findWorkspaceByUser(user);
        List<String> workSpaceList = workSpaceRepository.findByIdList(workSpaceIdList);

        return workSpaceList.stream()
                .map(WorkSpaceNameResponse::of)
                .collect(Collectors.toList());
    }

    public WorkSpaceResponse findWorkspaceById(AuthUser authUser, Long id) {

        User user = findUserByAuthUser(authUser);
        WorkSpace workSpace = findWorkSpaceById(id);

        isMember(user, workSpace);

        return WorkSpaceResponse.of(findWorkSpaceById(id));
    }

    @Transactional
    public WorkSpaceResponse updateWorkSpace(AuthUser authUser, Long id, WorkSpaceUpdateRequest request) {

        User user = findUserByAuthUser(authUser);
        WorkSpace workSpace = findWorkSpaceById(id);

        isMember(user, workSpace);
        isManage(user, workSpace);

        if (request.getName() != null) {
            workSpace.changeName(request.getName());
        }

        if (request.getDescription() != null) {
            workSpace.changeDescription(request.getDescription());
        }

        return WorkSpaceResponse.of(workSpace);
    }

    @Transactional
    public void deleteWorkSpace(AuthUser authUser, Long id) {
        User user = findUserByAuthUser(authUser);
        WorkSpace workSpace = findWorkSpaceById(id);

        isManage(user, workSpace);

        workSpaceRepository.deleteById(id);
    }

    @Transactional
    public void addMember(AuthUser authUser, Long id, AddMemberRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        WorkSpace workSpace = findWorkSpaceById(id);

        User manger = findUserByAuthUser(authUser);

        isManage(manger, workSpace);

        if (memberRepository.findByUserAndWorkspace(user, workSpace).isPresent()) {
            throw new ApiException(ErrorStatus._DUPLICATE_MANAGE);
        }

        memberRepository.save(new Member(user, workSpace, MemberRole.of(request.getMemberRole())));
    }

    private Member isMember(User user, WorkSpace workSpace) {
        return memberRepository.findByUserAndWorkspace(user, workSpace).orElseThrow(
                () -> new ApiException(ErrorStatus._INVALID_REQUEST)
        );
    }

    private User findUserByAuthUser(AuthUser authUser) {
        return userRepository.findById(authUser.getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_USER)
        );
    }

    private WorkSpace findWorkSpaceById(Long id) {
        return workSpaceRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE)
        );
    }

    public void isManage(User user, WorkSpace workSpace) {
        Member member = isMember(user, workSpace);

        if (member.getMemberRole() != MemberRole.MANAGE) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }
}
