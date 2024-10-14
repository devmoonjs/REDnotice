package io.rednotice.workspace.controller;

import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.ChangeMemberRoleRequest;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceResponse;
import io.rednotice.workspace.service.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    // Security 추가 후 admin 권한만 생성되도록 변경 예정
    @PostMapping("/workspaces")
    public ResponseEntity<WorkSpaceResponse> saveWorkSpace(@RequestBody WorkSpaceSaveRequest request) {

        WorkSpaceResponse response = workSpaceService.saveWorkSpace(request);

        return ResponseEntity.ok(response);
    }

    // Security 추가 후 해당 유저의 워크스페이스만 조회되도록 변경 예정
    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkSpaceResponse>> findAll() {

        return ResponseEntity.ok(workSpaceService.findAll());
    }

    // Security 추가 후 해당 유저의 워크스페이스만 조회되도록 변경 예정
    @GetMapping("/workspaces/{id}")
    public ResponseEntity<WorkSpaceResponse> saveWorkSpace(@PathVariable Long id) {

        return ResponseEntity.ok(workSpaceService.findById(id));
    }

    // Security 추가 후 해당 유저의 워크스페이스만 수정되도록 변경 예정
    @PostMapping("/workspaces/{id}")
    public ResponseEntity<WorkSpaceResponse> updateWorkSpace(
            @PathVariable Long id,
            @RequestBody WorkSpaceUpdateRequest request) {

        WorkSpaceResponse response = workSpaceService.updateWorkSpace(id, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/workspaces/{id}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable Long id) {

        workSpaceService.deleteWorSpace(id);

        return ResponseEntity.ok("워크스페이스가 삭제되었습니다.");
    }

    @PostMapping("/workspaces/{id}/invite")
    public ResponseEntity<String> addMember(
            @PathVariable Long id,
            @RequestBody AddMemberRequest request) {

        workSpaceService.addMember(id, request);

        return ResponseEntity.ok("멤버가 추가되었습니다.");
    }

    // Security 추가 후 ADMIN 권한만 멤버 권한 변경 가능하게 수정
    @PostMapping("/workspaces/{workSpaceId}/members/{memberId}/role")
    public ResponseEntity<String> addMember(
            @PathVariable Long workSpaceId,
            @PathVariable Long memberId,
            @RequestBody ChangeMemberRoleRequest request) {

        workSpaceService.changeMemberRole(workSpaceId, memberId, request);

        return ResponseEntity.ok("멤버가 추가되었습니다.");
    }
}
