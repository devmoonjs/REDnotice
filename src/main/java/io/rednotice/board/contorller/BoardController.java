package io.rednotice.board.contorller;

import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.response.WorkSpaceResponse;
import io.rednotice.workspace.service.WorkSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final WorkSpaceService workSpaceService;

    // Security 추가 후 admin 권한만 생성되도록 변경 예정
    @PostMapping("/boards")
    public ResponseEntity<WorkSpaceResponse> saveWorkSpace(@RequestBody WorkSpaceSaveRequest request) {

        WorkSpaceResponse response = workSpaceService.saveWorkSpace(request);

        return ResponseEntity.ok(response);
    }

    // 보드 리스트 조회
    @GetMapping("/boards")
    public ResponseEntity<List<WorkSpaceResponse>> findAll() {

        return ResponseEntity.ok(workSpaceService.findAll());
    }

    // 보드 단건 조회
//    @GetMapping("/boards/{boardid}")
//    public ResponseEntity<List<WorkSpaceResponse>> findAll() {
//
//        return ResponseEntity.ok(workSpaceService.findAll());
//    }

//    @DeleteMapping("/boards/{boardid}")

}
