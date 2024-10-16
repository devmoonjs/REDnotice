package io.rednotice.board.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardDeleteRequest;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.service.MemberService;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.service.WorkSpaceService;
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
    private final WorkSpaceService workSpaceService;
    private final MemberService memberService;

    @Transactional
    public BoardResponse saveBoard(BoardSaveRequest request) {
        WorkSpace workspace = workSpaceService.getWorkSpace(request.getWorkspaceId());
        Board board = boardRepository.save(new Board(request, workspace));

        return BoardResponse.of(board);
    }

    public List<BoardResponse> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponse::of)
                .collect(Collectors.toList());
    }

    public BoardResponse findById(Long id) {
        return BoardResponse.of(boardRepository.findBoardById(id));
    }


    @Transactional
    public BoardResponse updateBoard(AuthUser authUser,Long boardId, BoardUpdateRequest updateRequest) {
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());
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
        memberService.checkReadAndWrite(authuser.getId(), boardDeleteRequest.getWorkSpaceId());
        boardRepository.deleteById(id);
    }

    public Board getBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_BOARD)
        );
    }
}
