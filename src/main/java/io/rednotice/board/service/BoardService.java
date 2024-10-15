package io.rednotice.board.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.workspace.entity.WorkSpace;
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

    @Transactional
    public BoardResponse saveBoard(BoardSaveRequest request) {
        Board board = boardRepository.save(new Board(request));

        return BoardResponse.of(board);
    }

    public List<BoardResponse> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponse::of)
                .collect(Collectors.toList());
    }

    public BoardResponse findById(Long id) {
        return BoardResponse.of(findBoardById(id));
    }


    @Transactional
    public BoardResponse updateBoard(Long id, BoardUpdateRequest request) {
        Board board = findBoardById(id);

        if (request.getTitle() != null && request.getTitle().isEmpty()) {
            board.changeTitle(request.getTitle());
        }

        if (request.getColor() != null) {
            board.changeDescription(request.getColor());
        }

        return BoardResponse.of(board);
    }

    public void deleteBoard(Long id) {
        boardRepository.deleteById(id);
    }

    private Board findBoardById(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new NullPointerException("존재하지 않는 보드입니다.")
        );
    }
}
