package io.rednotice.board.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.board.request.BoardDeleteRequest;
import io.rednotice.board.request.BoardSaveRequest;
import io.rednotice.board.request.BoardUpdateRequest;
import io.rednotice.board.response.BoardResponse;
import io.rednotice.board.response.BoardSingleResponse;
import io.rednotice.board.service.BoardService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.dto.ReasonDto;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.service.MemberService;
import io.rednotice.user.enums.UserRole;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.service.WorkSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private WorkSpaceService workSpaceService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void saveBoard_ShouldSaveBoardAndReturnResponse() {
        // given
        BoardSaveRequest request = new BoardSaveRequest("Board Title", 1L, "blue");
        WorkSpace workspace = new WorkSpace(); // WorkSpace를 반환할 가짜 객체
        Board board = new Board(request, workspace);

        when(workSpaceService.getWorkSpace(1L)).thenReturn(workspace);
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        // when
        BoardResponse response = boardService.saveBoard(request);

        // then
        assertNotNull(response);
        assertEquals("Board Title", response.getTitle());
        verify(workSpaceService, times(1)).getWorkSpace(1L);
        verify(boardRepository, times(1)).save(any(Board.class));
    }

    @Test
    void saveBoard_ShouldThrowExceptionWhenTitleIsEmpty() {
        // given
        BoardSaveRequest request = new BoardSaveRequest("", 1L, "blue");

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            boardService.saveBoard(request);
        });
        ReasonDto reasonDto = ErrorStatus._INVALID_TITLE_REQUEST.getReasonHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, reasonDto.getHttpStatus());
    }

    @Test
    void updateBoard_ShouldThrowExceptionWhenTitleIsEmpty() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        BoardUpdateRequest request = new BoardUpdateRequest("", "red", 1L);

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            boardService.updateBoard(authUser, 1L, request);
        });

        ReasonDto reasonDto = ErrorStatus._INVALID_TITLE_REQUEST.getReasonHttpStatus();
        assertEquals(HttpStatus.BAD_REQUEST, reasonDto.getHttpStatus());
    }
    @Test
    void deleteBoard_ShouldDeleteBoard() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        doNothing().when(memberService).checkReadAndWrite(1L, 1L);
        doNothing().when(boardRepository).deleteById(1L);

        // when
        boardService.deleteBoard(authUser, 1L, new BoardDeleteRequest(1L));

        // then
        verify(memberService, times(1)).checkReadAndWrite(1L, 1L);
        verify(boardRepository, times(1)).deleteById(1L);
    }
    @Test
    void getBoardId_ShouldReturnBoardResponse() {
        // given
        Board board = new Board();
        board.changeTitle("Sample Board");
        when(boardRepository.findBoardById(1L)).thenReturn(board);

        // when
        BoardSingleResponse response = boardService.getBoardId(1L);

        // then
        assertNotNull(response);
        assertEquals("Sample Board", response.getTitle());
        verify(boardRepository, times(1)).findBoardById(1L);
    }

    // checkTitle은 saveBoard와 updateBoard에 호출되니까, 별도로 테스트하지 않고, 각 메서드에 통합해서 테스트함
}

