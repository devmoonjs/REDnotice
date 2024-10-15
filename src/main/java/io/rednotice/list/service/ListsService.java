package io.rednotice.list.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.repository.ListsRepository;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.workspace.entity.WorkSpace;
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
    private final BoardRepository boardRepository;

    @Transactional
    public ListsResponse saveLists(ListsSaveRequest request) {
        Board board = findBoardById(request.getBoardId());
        Lists lists = listsRepository.save(new Lists(request, board));

        return ListsResponse.of(lists);
    }

    public List<ListsResponse> findAll() {
        return listsRepository.findAll().stream()
                .map(ListsResponse::of)
                .collect(Collectors.toList());
    }

    public ListsResponse findById(Long id) {
        return ListsResponse.of(findListById(id));
    }

    @Transactional
    public ListsResponse updateLists(Long id, ListsUpdateRequest request) {
        Lists lists = findListById(id);

        if (request.getName() != null && request.getName().isEmpty()) {
            lists.changeName(request.getName());
        }

        if (request.getSequence() != 0) {
            lists.changeSequence(request.getSequence());
        }

        return ListsResponse.of(lists);
    }

    public void deleteList(Long id) {
        listsRepository.deleteById(id);
    }

    private Lists findListById(Long id) {
        return listsRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_LISTS)
        );
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_BOARD)
        );
    }
}
