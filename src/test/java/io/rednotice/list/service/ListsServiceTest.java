package io.rednotice.list.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.service.BoardService;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.dto.ReasonDto;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.repository.ListsRepository;
import io.rednotice.list.request.ListDeleteRequest;
import io.rednotice.list.request.ListsSaveRequest;
import io.rednotice.list.request.ListsUpdateRequest;
import io.rednotice.list.response.ListsResponse;
import io.rednotice.member.service.MemberService;
import io.rednotice.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ListsServiceTest {

    @Mock
    private ListsRepository listsRepository;

    @Mock
    private BoardService boardService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private ListsService listsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Mockito 초기화
    }

    @Test
    void saveLists_ShouldSaveListSuccessfully() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        ListsSaveRequest request = new ListsSaveRequest(1L, 1L, "New List",1);
        Board board = new Board();
        Lists list = new Lists(request, board);

        // when
        when(boardService.getBoardById(1L)).thenReturn(board);
        when(listsRepository.save(any(Lists.class))).thenReturn(list);
        doNothing().when(memberService).checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());

        // when
        ListsResponse response = listsService.saveLists(authUser, request);

        // then
        assertNotNull(response);
        assertEquals("New List", response.getName());
        verify(boardService, times(1)).getBoardById(1L);
        verify(listsRepository, times(1)).save(any(Lists.class));
    }

    @Test
    void updateLists_ShouldUpdateListSuccessfully() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        ListsUpdateRequest request = new ListsUpdateRequest(1L, "Updated List", 2);
        Lists existingList = new Lists();
        existingList.changeName("Old List");
        existingList.changeSequence(1);

        List<Lists> allListsInBoard = List.of(existingList);

        // when
        when(listsRepository.findById(1L)).thenReturn(Optional.of(existingList));
        when(listsRepository.findAllByBoard_WorkspaceId(1L)).thenReturn(allListsInBoard);
        doNothing().when(memberService).checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());

        // when
        ListsResponse response = listsService.updateLists(authUser, 1L, request);

        // then
        assertNotNull(response);
        assertEquals("Updated List", response.getName());
        assertEquals(2, response.getSequence());
        verify(listsRepository, times(1)).findById(1L);
        verify(listsRepository, times(1)).findAllByBoard_WorkspaceId(1L);
    }

    @Test
    void deleteList_ShouldDeleteListSuccessfully() {
        // given
        AuthUser authUser = new AuthUser(1L, "user@example.com", UserRole.USER);
        ListDeleteRequest request = new ListDeleteRequest(1L);

        doNothing().when(memberService).checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());
        doNothing().when(listsRepository).deleteById(1L);

        // when
        listsService.deleteList(authUser, 1L, request);

        // then
        verify(memberService, times(1)).checkReadAndWrite(authUser.getId(), request.getWorkSpaceId());
        verify(listsRepository, times(1)).deleteById(1L);
    }
    @Test
    void getListById_ShouldThrowExceptionWhenListNotFound() {
        // given
        Long listId = 999L;

        when(listsRepository.findById(listId)).thenReturn(Optional.empty());

        // when & then
        ApiException exception = assertThrows(ApiException.class, () -> {
            listsService.getListById(listId);
        });

        ReasonDto reasonDto = ErrorStatus._NOT_FOUND_LISTS.getReasonHttpStatus();
        assertEquals(HttpStatus.NOT_FOUND, reasonDto.getHttpStatus());
        verify(listsRepository, times(1)).findById(listId);
    }

}
