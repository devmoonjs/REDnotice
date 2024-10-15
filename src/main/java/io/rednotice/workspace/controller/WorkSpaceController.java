package io.rednotice.workspace.controller;

import io.rednotice.common.AuthUser;
import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceNameResponse;
import io.rednotice.workspace.response.WorkSpaceResponse;
import io.rednotice.workspace.service.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkSpaceNameResponse>> findWorkSpaces(
            @AuthenticationPrincipal AuthUser authUser) {

        return ResponseEntity.ok(workSpaceService.findWorkSpaces(authUser));
    }

    @GetMapping("/workspaces/{id}")
    public ResponseEntity<WorkSpaceResponse> findWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id) {

        return ResponseEntity.ok(workSpaceService.findWorkspaceById(authUser, id));
    }

    // Security 추가 후 해당 유저의 워크스페이스만 수정되도록 변경 예정
    @PatchMapping("/workspaces/{id}")
    public ResponseEntity<WorkSpaceResponse> updateWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @RequestBody WorkSpaceUpdateRequest request) {

        WorkSpaceResponse response = workSpaceService.updateWorkSpace(authUser, id, request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/workspaces/{id}/invite")
    public ResponseEntity<String> addMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @RequestBody AddMemberRequest request) {

        workSpaceService.addMember(authUser, id, request);

        return ResponseEntity.ok("멤버가 추가되었습니다.");
    }

    @DeleteMapping("/workspaces/{id}")
    public ResponseEntity<String> deleteWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id) {

        workSpaceService.deleteWorkSpace(authUser, id);

        return ResponseEntity.ok("워크스페이스가 삭제되었습니다.");
    }
}
