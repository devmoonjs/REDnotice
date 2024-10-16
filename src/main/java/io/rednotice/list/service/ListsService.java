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
        // 사용자 권한 체크
        checkMemberRole(authUser.getId(), updateRequest.getWorkSpaceId());

        // 리스트가 존재하는지 확인
        Lists lists = findListById(id);

        // 리스트가 속한 보드의 리스트들을 모두 가져옴 (보드별 시퀀스 관리가 필요하므로)
        List<Lists> allListsInBoard = listsRepository.findAllByBoard_WorkspaceId(updateRequest.getWorkSpaceId());

        // 리스트 제목이 존재하고 비어 있지 않으면 제목 변경
        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            lists.changeName(updateRequest.getName());
        } else {
            throw new IllegalArgumentException("리스트 제목은 반드시 입력해야 합니다.");
        }

        // 순서가 변경될 때만 시퀀스 업데이트 처리
        if (updateRequest.getSequence() > 0 && updateRequest.getSequence() != lists.getSequence()) {
            // 시퀀스가 변경되었을 때 기존 리스트들의 시퀀스를 조정
            updateListSequence(allListsInBoard, lists, updateRequest.getSequence());
        }

        return ListsResponse.of(lists);
    }

    // 시퀀스 변경 시 다른 리스트들의 순서 업데이트
    private void updateListSequence(List<Lists> allListsInBoard, Lists updatedList, int newSequence) {
        int currentSequence = updatedList.getSequence();

        if (newSequence > currentSequence) {
            // 새로운 시퀀스가 기존 시퀀스보다 클 경우 (즉, 아래로 내려가는 경우)
            for (Lists list : allListsInBoard) {
                if (list.getSequence() > currentSequence && list.getSequence() <= newSequence) {
                    list.changeSequence(list.getSequence() - 1);  // 시퀀스를 하나씩 당김
                }
            }
        } else {
            // 새로운 시퀀스가 기존 시퀀스보다 작을 경우 (즉, 위로 올라가는 경우)
            for (Lists list : allListsInBoard) {
                if (list.getSequence() < currentSequence && list.getSequence() >= newSequence) {
                    list.changeSequence(list.getSequence() + 1);  // 시퀀스를 하나씩 밀어냄
                }
            }
        }

        // 마지막으로 현재 리스트의 시퀀스를 새로운 시퀀스로 변경
        updatedList.changeSequence(newSequence);
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
