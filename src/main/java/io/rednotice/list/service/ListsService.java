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

    public static int count;

    @Transactional
    public ListsResponse saveLists(AuthUser authUser, ListsSaveRequest request) {
        memberService.checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());

        Board board = boardService.getBoardById(request.getBoardId());
        Lists lists = listsRepository.save(new Lists(request, board));

        return ListsResponse.of(lists);
    }

    public List<ListsResponse> getAllLists() {
        return listsRepository.findAll().stream()
                .map(ListsResponse::of)
                .collect(Collectors.toList());
    }

    public ListsResponse getId(Long id) {
        return ListsResponse.of(getListById(id));
    }

    @Transactional
    public ListsResponse updateLists(AuthUser authUser, Long id, ListsUpdateRequest updateRequest) {
        // 사용자 권한 체크
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());

        // 리스트가 존재하는지 확인
        Lists lists = getListById(id);

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
    public ListsResponse updateListsWithPessimisticLock(AuthUser authUser, Long id, ListsUpdateRequest updateRequest) {
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());

        // 비관적 락을 사용하여 리스트를 조회
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_LISTS));

        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            lists.changeName(updateRequest.getName());
        } else {
            throw new IllegalArgumentException("리스트 제목은 반드시 입력해야 합니다.");
        }

        if (updateRequest.getSequence() > 0 && updateRequest.getSequence() != lists.getSequence()) {
            lists.changeSequence(updateRequest.getSequence());
        }

        count++;
        return ListsResponse.of(lists);
    }

    @Transactional
    public ListsResponse updateList(AuthUser authUser, Long id, ListsUpdateRequest updateRequest) {
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());

        // 비관적 락을 사용하여 리스트를 조회
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_LISTS));

        if (updateRequest.getName() != null && !updateRequest.getName().isEmpty()) {
            lists.changeName(updateRequest.getName());
        } else {
            throw new IllegalArgumentException("리스트 제목은 반드시 입력해야 합니다.");
        }

        if (updateRequest.getSequence() > 0 && updateRequest.getSequence() != lists.getSequence()) {
            lists.changeSequence(updateRequest.getSequence());
        }

        count++;
        return ListsResponse.of(lists);
    }

    ///

    ///



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

//. 비관적 락 동시성 제어 테스트용
//    public static void set(int i){
//        count = i;
//    }
//
//    public static int getCount(){
//        return count;
//    }

    @Transactional
    public void lockListAndHold(Long id) throws InterruptedException {
        // 비관적 락을 사용하여 리스트를 조회
        Lists lists = listsRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_LISTS));

        // 일정 시간 동안 트랜잭션을 유지
        System.out.println("리스트를 비관적 락으로 잠금. 잠금 유지 중...");
        Thread.sleep(5000); // 5초 동안 트랜잭션 유지 (예시)
        System.out.println("비관적 락 해제");
    }

}
