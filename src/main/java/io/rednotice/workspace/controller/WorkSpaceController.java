package io.rednotice.workspace.controller;

import io.rednotice.common.AuthUser;
import io.rednotice.common.anotation.SlackNotify;
import io.rednotice.common.apipayload.ApiResponse;
import io.rednotice.workspace.request.AddMemberRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceNameResponse;
import io.rednotice.workspace.response.WorkSpaceResponse;
import io.rednotice.workspace.service.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkSpaceController {

    private final WorkSpaceService workSpaceService;

    @GetMapping("/workspaces/v1")
    public ApiResponse<List<WorkSpaceNameResponse>> findWorkSpaces(
            @AuthenticationPrincipal AuthUser authUser) {

        return ApiResponse.ok(workSpaceService.findWorkSpaces(authUser));
    }

    @GetMapping("/workspaces/v1/{id}")
    public ApiResponse<WorkSpaceResponse> findWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id) {

        return ApiResponse.ok(workSpaceService.findWorkspace(authUser, id));
    }

    @PatchMapping("/workspaces/v1/{id}")
    public ApiResponse<WorkSpaceResponse> updateWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @RequestBody WorkSpaceUpdateRequest request) {

        WorkSpaceResponse response = workSpaceService.updateWorkSpace(authUser, id, request);

        return ApiResponse.ok(response);
    }

    @SlackNotify
    @PostMapping("/workspaces/v1/{id}/invite")
    public ApiResponse<String> addMember(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @RequestBody AddMemberRequest request) {

        workSpaceService.addMember(authUser, id, request);

        return ApiResponse.ok("멤버가 추가되었습니다.");
    }

    @DeleteMapping("/workspaces/v1/{id}")
    public ApiResponse<String> deleteWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id) {

        workSpaceService.deleteWorkSpace(authUser, id);

        return ApiResponse.ok("워크스페이스가 삭제되었습니다.");
    }
}
