package io.rednotice.list.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.repository.ListsRepository;
import io.rednotice.list.request.ListDeleteRequest;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
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
    private final MemberRepository memberRepository;

    @Transactional
    public ListsResponse saveLists(AuthUser authUser, ListsSaveRequest request) {
        checkMemberRole(authUser.getId(), request.getWorkSpaceId());

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
    public ListsResponse updateLists(AuthUser authUser, Long id, ListsUpdateRequest updateRequest) {
        checkMemberRole(authUser.getId(), updateRequest.getWorkSpaceId());
        Lists lists = findListById(id);

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
        checkMemberRole(authUser.getId(), deleteRequest.getWorkSpaceId());
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

    private void checkMemberRole(Long userId, Long workSpaceId) {
        // 읽기 전용 역할인 경우 예외 발생
        Member member = memberRepository.getMember(userId, workSpaceId);
        if (member.getMemberRole().equals(MemberRole.READ)) {
            throw new ApiException(ErrorStatus._READ_ONLY_ROLE);
        }
    }
}
