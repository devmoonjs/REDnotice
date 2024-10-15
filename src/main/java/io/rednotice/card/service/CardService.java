package io.rednotice.card.service;

import io.rednotice.board.entity.Board;
import io.rednotice.card.dto.request.*;
import io.rednotice.card.dto.response.CardDetailResponse;
import io.rednotice.card.dto.response.CardManagerResponse;
import io.rednotice.card.dto.response.CardResponse;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.user.repository.UserRepository;
import io.rednotice.workspace.enums.MemberRole;
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
//    private final ListRepository listRepository;
//    private final BoardRepository boardRepository;


    @Transactional
    public CardResponse saveCard(AuthUser authUser, CardSaveRequest saveRequest) {
        checkMemberRole(authUser.getId(), saveRequest.getWorkSpaceId());

        Board board = new Board();  // 임시 Board 객체(repo 구현되면 수정!)
        Lists list = new Lists();   // 임시 Lists 객체(repo 구현되면 수정!)
        User user = userRepository.getUserById(authUser.getId());
        Card card = new Card(
                saveRequest.getTitle(),
                saveRequest.getDescription(),
                saveRequest.getDueDate(),
                saveRequest.getSeq(),
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
