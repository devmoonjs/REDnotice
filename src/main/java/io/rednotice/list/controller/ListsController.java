package io.rednotice.list.controller;

import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.list.service.ListsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ListsController {
    private final ListsService listsService;

    // 리스트 생성
    @PostMapping("/lists")
    private ResponseEntity<ListsResponse> saveList(@RequestBody ListsSaveRequest request) {
        ListsResponse response = listsService.saveLists(request);
        return ResponseEntity.ok(response);
    }

    // 다건 조회
    @GetMapping("/lists")
    private ResponseEntity<List<ListsResponse>> findAllLists() {
        return ResponseEntity.ok(listsService.findAll());
    }

    // 단건 조회
    @GetMapping("/lists/{listId}")
    private ResponseEntity<ListsResponse> getListId(@PathVariable Long listId) {
        return ResponseEntity.ok(listsService.findById(listId));
    }

    // 리스트 수정
    @PatchMapping("/lists/{listId}")
    private ResponseEntity<ListsResponse> updateList(@PathVariable Long listId, @RequestBody ListsUpdateRequest request) {
        return ResponseEntity.ok(listsService.updateLists(listId, request));
    }

    // 리스트 삭제
    @DeleteMapping("/lists/{listId}")
    private ResponseEntity<String> deleteList(@PathVariable Long listId) {
        listsService.deleteList(listId);
        return ResponseEntity.ok("리스트가 정상적으로 삭제되었습니다.");
    }
}
