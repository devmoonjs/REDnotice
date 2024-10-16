package io.rednotice.list.controller;

import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.ApiResponse;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.list.service.ListsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ListsController {
    private final ListsService listsService;

    // 리스트 생성
    @PostMapping("/lists")
    private ApiResponse<ListsResponse> saveList(@AuthenticationPrincipal AuthUser authUser,
                                                @RequestBody ListsSaveRequest request) {
        ListsResponse response = listsService.saveLists(authUser,request);
        return ApiResponse.ok(response);
    }

    // 다건 조회
    @GetMapping("/lists")
    private ApiResponse<List<ListsResponse>> findAllLists() {
        return ApiResponse.ok(listsService.findAll());
    }

    // 단건 조회
    @GetMapping("/lists/{listId}")
    private ApiResponse<ListsResponse> getListId(@PathVariable Long listId) {
        return ApiResponse.ok(listsService.findById(listId));
    }

    // 리스트 수정
    @PatchMapping("/lists/{listId}")
    private ApiResponse<ListsResponse> updateList(@PathVariable Long listId, @RequestBody ListsUpdateRequest request) {
        return ApiResponse.ok(listsService.updateLists(listId, request));
    }

    // 리스트 삭제
    @DeleteMapping("/lists/{listId}")
    private ApiResponse<String> deleteList(@PathVariable Long listId) {
        listsService.deleteList(listId);
        return ApiResponse.ok("리스트가 정상적으로 삭제되었습니다.");
    }
}
