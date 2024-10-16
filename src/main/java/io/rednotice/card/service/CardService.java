package io.rednotice.card.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.repository.BoardRepository;
import io.rednotice.card.dto.request.*;
import io.rednotice.card.dto.response.*;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.repository.ListsRepository;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.user.repository.UserRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final ListsRepository listsRepository;
    private final BoardRepository boardRepository;
    private final WorkSpaceRepository workSpaceRepository;

    /**
     * 1. 댓글기능 접근 시 멤버권한 확인해야함
     * -> 이걸 위해 WorkSpace id를 객체에 추가함
     * -> 기능개발 완료 후 AOP로 멤버권한 체크 로직을 분리!
     * 2. 모든 도메인에서 member role 체크가 있음.
     * -> 이걸 위해 MemberRepository를 모든 도메인에서 참조
     * -> 이것도 기능개발 완료 후 AOP 또는 다른방법으로 로직을 분리!
     * 3. 참조가 많으니 service 레이어를 분리하자!
     * -> Component Service, Module Service 로 분리 (파사트 패턴)
     */

    @Transactional
    public CardResponse saveCard(AuthUser authUser, CardSaveRequest saveRequest) {
        checkMemberRole(authUser.getId(), saveRequest.getWorkSpaceId());

        WorkSpace workSpace = workSpaceRepository.getWorkspaceById(saveRequest.getWorkSpaceId());
        Board board = boardRepository.findById(saveRequest.getBoardId()).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_BOARD));
        Lists list = listsRepository.findById(saveRequest.getListId()).orElseThrow(() -> new ApiException(ErrorStatus._NOT_FOUND_LISTS));
        User user = userRepository.getUserById(authUser.getId());
        Card card = new Card(
                saveRequest.getTitle(),
                saveRequest.getDescription(),
                saveRequest.getDueDate(),
                saveRequest.getSeq(),
                workSpace,
                board,
                list,
                user
        );
        Card saveCard = cardRepository.save(card);

        return CardResponse.of(saveCard);
    }

    @Transactional
    public CardManagerResponse changeManager(AuthUser authUser, Long cardId, CardManagerRequest managerRequest) {
        checkMemberRole(authUser.getId(), managerRequest.getWorkSpaceId());

        User manager = userRepository.getUserById(managerRequest.getManagerId());
        Card card = cardRepository.getCardById(cardId);
        card.changeManager(manager);

        return new CardManagerResponse(cardId, manager.getId());
    }

    public Page<CardSearchDto> searchCards(CardPageRequest cardPageRequest, CardSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(cardPageRequest.getPage(), cardPageRequest.getSize());
        return cardRepository.search(
                searchRequest.getTitle(),
                searchRequest.getDescription(),
                searchRequest.getDueDate(),
                searchRequest.getManagerName(),
                searchRequest.getBoardId(),
                pageable
        );
    }

    public CardDetailResponse getCard(Long cardId) {
        Card card = cardRepository.getCardById(cardId);
        return CardDetailResponse.of(card);
    }

    @Transactional
    public CardResponse updateCard(AuthUser authUser, Long cardId, CardUpdateRequest updateRequest) {
        checkMemberRole(authUser.getId(), updateRequest.getWorkSpaceId());
        Card card = cardRepository.getCardById(cardId);
        card.updateCard(updateRequest.getTitle(), updateRequest.getDescription(), updateRequest.getDueDate());
        return CardResponse.of(card);
    }

    @Transactional
    public void deleteCard(AuthUser authUser, Long cardId, CardDeleteRequest deleteRequest) {
        checkMemberRole(authUser.getId(), deleteRequest.getWorkSpaceId());
        cardRepository.deleteById(cardId);
    }

    private void checkMemberRole(Long userId, Long workSpaceId) {
        // 읽기 전용 역할인 경우 예외 발생
        Member member = memberRepository.getMember(userId, workSpaceId);
        if (member.getMemberRole().equals(MemberRole.READ)) {
            throw new ApiException(ErrorStatus._READ_ONLY_ROLE);
        }
    }
}
