package io.rednotice.board.contorller;

import io.rednotice.board.request.BoardDeleteRequest;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.board.service.BoardService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/boards")
    public ApiResponse<BoardResponse> saveBoard(@RequestBody BoardSaveRequest request) {
        BoardResponse response = boardService.saveBoard(request);

        return ApiResponse.ok(response);
    }

    // 보드 리스트 조회
    @GetMapping("/boards")
    public ApiResponse<List<BoardResponse>> findAll() {

        return ApiResponse.ok(boardService.findAll());
    }

    // 보드 단건 조회
    @GetMapping("/boards/{boardId}")
    public ApiResponse<BoardResponse> getBoard(@PathVariable Long boardId) {

        return ApiResponse.ok(boardService.findById(boardId));
    }

    // 보드 내용(color, title) 업데이트
    @PatchMapping("/boards/{boardId}")
    public ApiResponse<BoardResponse> updateBoard(@AuthenticationPrincipal AuthUser authUser,
                                                  @PathVariable Long boardId,
                                                  @RequestBody BoardUpdateRequest boardUpdateRequest) {
        return ApiResponse.ok(boardService.updateBoard(authUser, boardId, boardUpdateRequest));
    }

    //보드 삭제
    @DeleteMapping("/boards/{boardId}")
    public ApiResponse<String> deleteBoard(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable Long boardId,
                                           @RequestBody BoardDeleteRequest boardDeleteRequest) {
        boardService.deleteBoard(authUser, boardId, boardDeleteRequest);
        return ApiResponse.ok("보드가 정상적으로 삭제되었습니다.");
    }
}
