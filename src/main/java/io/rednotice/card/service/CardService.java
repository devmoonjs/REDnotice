package io.rednotice.card.service;

import io.rednotice.board.entity.Board;
import io.rednotice.board.service.BoardService;
import io.rednotice.card.dto.request.*;
import io.rednotice.card.dto.response.*;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.common.AuthUser;
import io.rednotice.list.entity.Lists;
import io.rednotice.list.service.ListsService;
import io.rednotice.member.service.MemberService;
import io.rednotice.user.entity.User;
import io.rednotice.user.service.UserService;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.service.WorkSpaceService;
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
    private final UserService userService;
    private final MemberService memberService;
    private final ListsService listsService;
    private final BoardService boardService;
    private final WorkSpaceService workSpaceService;

    /**
     * 1. 모든 도메인에서 member role 체크가 있음.
     * -> 이걸 위해 MemberRepository를 모든 도메인에서 참조
     * -> 이것도 기능개발 완료 후 AOP 또는 다른방법으로 로직을 분리!
     * 2. 참조가 많으니 repository 말고 service를 주입받자!
     * -> Component Service, Module Service 로 분리 (파사트 패턴)는 최종 때 해보기
     */

    @Transactional
    public CardResponse saveCard(AuthUser authUser, CardSaveRequest saveRequest) {
        memberService.checkReadAndWrite(authUser.getId(), saveRequest.getWorkSpaceId());

        WorkSpace workSpace = workSpaceService.getWorkSpace(saveRequest.getWorkSpaceId());
        Board board = boardService.getBoardById(saveRequest.getBoardId());
        Lists list = listsService.getListById(saveRequest.getListId());
        User user = userService.getUser(authUser.getId());
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
        memberService.checkReadAndWrite(authUser.getId(), managerRequest.getWorkSpaceId());

        User manager = userService.getUser(managerRequest.getManagerId());
        Card card = getCardById(cardId);
        card.changeManager(manager);

        return new CardManagerResponse(cardId, manager.getId());
    }

    public Page<CardSearchDto> searchCards(CardPageRequest cardPageRequest, CardSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(cardPageRequest.getPage() - 1, cardPageRequest.getSize());
        return cardRepository.search(
                searchRequest.getTitle(),
                searchRequest.getDescription(),
                searchRequest.getDueDate(),
                searchRequest.getManagerName(),
                searchRequest.getBoardId(),
                pageable
        );
    }

    @Transactional
    public CardDetailResponse getCard(Long cardId) {
        Card card = getCardById(cardId);
        // 조회수 1 증가
        card.updateViews(card.getViews() + 1);
        return CardDetailResponse.of(card);
    }

    @Transactional
    public CardResponse updateCard(AuthUser authUser, Long cardId, CardUpdateRequest updateRequest) {
        memberService.checkReadAndWrite(authUser.getId(), updateRequest.getWorkSpaceId());
        Card card = getCardById(cardId);
        card.updateCard(updateRequest.getTitle(), updateRequest.getDescription(), updateRequest.getDueDate());
        return CardResponse.of(card);
    }

    @Transactional
    public void deleteCard(AuthUser authUser, Long cardId, CardDeleteRequest deleteRequest) {
        memberService.checkReadAndWrite(authUser.getId(), deleteRequest.getWorkSpaceId());
        cardRepository.deleteById(cardId);
    }

    public Card getCardById(Long cardId) {
        return cardRepository.findCardById(cardId);
    }

}
