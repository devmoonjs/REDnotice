package io.rednotice.workspace.controller;

import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import io.rednotice.user.enums.UserRole;
import io.rednotice.workspace.request.ChangeMemberRoleRequest;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.response.WorkSpaceResponse;
import io.rednotice.workspace.service.WorkSpaceAdminService;
import io.rednotice.workspace.service.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WorkSpaceAdminController {

    private final WorkSpaceAdminService workSpaceAdminService;

    @Secured(UserRole.Authority.ADMIN)
    @PostMapping("/workspaces/v1")
    public ApiResponse<WorkSpaceResponse> saveWorkSpace(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody WorkSpaceSaveRequest request) {

        WorkSpaceResponse response = workSpaceAdminService.saveWorkSpace(authUser, request);

        return ApiResponse.ok(response);
    }

    @Secured(UserRole.Authority.ADMIN)
    @PatchMapping("/workspaces/v1{workSpaceId}/members/{memberId}/role")
    public ApiResponse<String> changeMemberRole(
            @PathVariable Long workSpaceId,
            @PathVariable Long memberId,
            @RequestBody ChangeMemberRoleRequest request) {

        workSpaceAdminService.changeMemberRole(workSpaceId, memberId, request);

        return ApiResponse.ok("권한 변경되었습니다.");
    }
}
