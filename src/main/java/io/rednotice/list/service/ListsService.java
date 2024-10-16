package io.rednotice.list.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.service.BoardService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.repository.ListsRepository;
import io.rednotice.list.request.ListDeleteRequest;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListsService {
    private final ListsRepository listsRepository;
    private final BoardService boardService;
    private final MemberService memberService;

    @Transactional
    public ListsResponse saveLists(AuthUser authUser, ListsSaveRequest request) {
        memberService.checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());

        Board board = boardService.getBoardById(request.getBoardId());
        Lists lists = listsRepository.save(new Lists(request, board));

        return ListsResponse.of(lists);
    }

    public List<ListsResponse> findAll() {
        return listsRepository.findAll().stream()
                .map(ListsResponse::of)
                .collect(Collectors.toList());
    }

    public ListsResponse findById(Long id) {
        return ListsResponse.of(getListById(id));
    }

    @Transactional
    public ListsResponse updateLists(AuthUser authUser, Long id, ListsUpdateRequest updateRequest) {
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());
        Lists lists = getListById(id);

        if (updateRequest.getName() != null && updateRequest.getName().isEmpty()) {
            lists.changeName(updateRequest.getName());
        }

        if (updateRequest.getSequence() != 0) {
            lists.changeSequence(updateRequest.getSequence());
        }

        return ListsResponse.of(lists);
    }

    @Transactional
    public void deleteList(AuthUser authUser, Long id, ListDeleteRequest deleteRequest) {
        memberService.checkReadAndWrite(authUser.getId(), deleteRequest.getWorkSpaceId());
        listsRepository.deleteById(id);
    }

    public Lists getListById(Long id) {
        return listsRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_LISTS)
        );
    }

}
