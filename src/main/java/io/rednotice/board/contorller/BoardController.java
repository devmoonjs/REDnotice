package io.rednotice.board.contorller;

import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    // 보드 생성
    @PostMapping("/boards")
    public ResponseEntity<BoardResponse> saveBoard(@RequestBody BoardSaveRequest request) {
        BoardResponse response = boardService.saveBoard(request);

        return ResponseEntity.ok(response);
    }

    // 보드 리스트 조회
    @GetMapping("/boards")
    public ResponseEntity<List<BoardResponse>> findAll() {

        return ResponseEntity.ok(boardService.findAll());
    }

    // 보드 단건 조회
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {

        return ResponseEntity.ok(boardService.findById(boardId));
    }

    // 보드 내용(color, title) 업데이트
    @PatchMapping("/boards/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId, @RequestBody BoardUpdateRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, request));
    }

    //보드 삭제
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("보드가 정상적으로 삭제되었습니다.");
    }
}
