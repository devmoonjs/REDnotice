package io.rednotice.board.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardDeleteRequest;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.board.response.BoardSingleResponse;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import io.rednotice.workspace.request.WorkSpaceSaveRequest;
import io.rednotice.workspace.request.WorkSpaceUpdateRequest;
import io.rednotice.workspace.response.WorkSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public BoardResponse saveBoard(BoardSaveRequest request) {
        checkTitle(request.getTitle()); // 예외처리 : 제목(title)이 없는 경우
        WorkSpace workspace = findWorkSpaceById(request.getWorkspaceId());
        Board board = boardRepository.save(new Board(request, workspace));

        return BoardResponse.of(board);
    }

    public List<BoardResponse> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponse::of)
                .collect(Collectors.toList());
    }

    public BoardSingleResponse findById(Long id) {
        return BoardSingleResponse.of(boardRepository.findBoardById(id));
    }


    @Transactional
    public BoardResponse updateBoard(AuthUser authUser,Long boardId, BoardUpdateRequest updateRequest) {
        checkTitle(updateRequest.getTitle()); // 예외처리 : 제목(title)이 없는 경우
        checkMemberRole(authUser.getId(), updateRequest.getWorkSpaceId());
        Board board = boardRepository.findBoardById(boardId);

        if (updateRequest.getTitle() != null && updateRequest.getTitle().isEmpty()) {
            board.changeTitle(updateRequest.getTitle());
        }

        if (updateRequest.getColor() != null) {
            board.changeColor(updateRequest.getColor());
        }

        return BoardResponse.of(board);
    }

    @Transactional
    public void deleteBoard(AuthUser authuser, Long id, BoardDeleteRequest boardDeleteRequest) {
        checkMemberRole(authuser.getId(), boardDeleteRequest.getWorkSpaceId());
        boardRepository.deleteById(id);
    }

    private WorkSpace findWorkSpaceById(Long id) {
        return workSpaceRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_WORKSPACE)
        );
    }

    private void checkMemberRole(Long userId, Long workSpaceId) {
        // 읽기 전용 역할인 경우 예외 발생
        Member member = memberRepository.getMember(userId, workSpaceId);
        if (member.getMemberRole().equals(MemberRole.READ)) {
            throw new ApiException(ErrorStatus._READ_ONLY_ROLE);
        }
    }

    private void checkTitle(String title) {
        if (title.isEmpty()) {
            throw new ApiException(ErrorStatus._INVALID_TITLE_REQUEST);
        }
    }
}
