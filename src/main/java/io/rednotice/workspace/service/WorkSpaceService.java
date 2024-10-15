package io.rednotice.workspace.service;

import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.user.enums.UserRole;
import io.rednotice.user.repository.UserRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.ChangeMemberRoleRequest;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
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
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkSpaceResponse saveWorkSpace(WorkSpaceSaveRequest request) {
        WorkSpace workSpace = workSpaceRepository.save(new WorkSpace(request));

        return WorkSpaceResponse.of(workSpace);
    }

    public List<WorkSpaceResponse> findAll() {
        return workSpaceRepository.findAll().stream()
                .map(WorkSpaceResponse::of)
                .collect(Collectors.toList());
    }

    public WorkSpaceResponse findById(Long id) {
        return WorkSpaceResponse.of(findWorkSpaceById(id));
    }

    @Transactional
    public WorkSpaceResponse updateWorkSpace(Long id, WorkSpaceUpdateRequest request) {
        WorkSpace workSpace = findWorkSpaceById(id);

        if (request.getName() != null && request.getName().isEmpty()) {
            workSpace.changeName(request.getName());
        }

        if (request.getDescription() != null) {
            workSpace.changeDescription(request.getDescription());
        }

        return WorkSpaceResponse.of(workSpace);
    }

    public void deleteWorSpace(Long id) {
        workSpaceRepository.deleteById(id);
    }

    public void addMember(Long id, AddMemberRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        isAdmin(user);

        WorkSpace workSpace = findWorkSpaceById(id);

        memberRepository.save(new Member(user, workSpace, MemberRole.of(request.getMemberRole())));
    }



    private void isAdmin(User user) {
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("ADMIN 권한만 워크스페이스 생성이 가능합니다.");
        }
    }

    public void changeMemberRole(Long workSpaceId, Long memberId, ChangeMemberRoleRequest request) {
        WorkSpace workSpace = findWorkSpaceById(workSpaceId);
        User user = getUserById(memberId);

        Member member = memberRepository.findByUserAndWorkSpace(user, workSpace);

        member.changeRole(request.getMemberRole());
    }

    private User getUserById(Long memberId) {
        return userRepository.findById(memberId).orElseThrow(
                () -> new NullPointerException("존재하지 않는 유저입니다.")
        );
    }

    private WorkSpace findWorkSpaceById(Long id) {
        return workSpaceRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 워크스페이스입니다.")
        );
    }
}
