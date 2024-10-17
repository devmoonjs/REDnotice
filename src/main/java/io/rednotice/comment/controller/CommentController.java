package io.rednotice.comment.controller;

import io.rednotice.comment.request.CommentRequest;
import io.rednotice.comment.request.CommentUpdateRequest;
import io.rednotice.comment.response.CommentResponse;
import io.rednotice.comment.service.CommentService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/v1")
    public ApiResponse<CommentResponse> saveComment(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CommentRequest request
            ) {

        return ApiResponse.ok(commentService.saveComment(authUser, request));
    }

    @PatchMapping("/comments/v1/{id}")
    public ApiResponse<CommentResponse> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody CommentUpdateRequest request,
            @PathVariable Long id
            ) {

        return ApiResponse.ok(commentService.updateComment(authUser, request, id));
    }

    @DeleteMapping("/comments/v1/{id}")
    public ApiResponse<String> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id
    ) {

        commentService.deleteComment(authUser, id);

        return ApiResponse.ok("댓글이 삭제 되었습니다.");
    }
}
