package io.rednotice.card.service;

import io.rednotice.board.entity.Board;
import io.rednotice.card.dto.request.*;
import io.rednotice.card.dto.response.CardManagerResponse;
import io.rednotice.card.dto.response.CardResponse;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.list.entity.Lists;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.user.entity.User;
import io.rednotice.user.repository.UserRepository;
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

        return CardResponse.of(saveCard, user);
    }

    @Transactional
    public CardManagerResponse addManager(AuthUser authUser, Long cardId, CardManagerRequest managerRequest) {
        checkMemberRole(authUser.getId(), managerRequest.getWorkSpaceId());

        User manager = userRepository.getUserById(managerRequest.getManagerId());
        Card card = cardRepository.getCardById(cardId);
        card.changeManager(manager);

        return new CardManagerResponse(cardId, manager.getId());
    }


    @Transactional
    public void deleteCard(AuthUser authUser, Long cardId, CardDeleteRequest deleteRequest) {
        checkMemberRole(authUser.getId(), deleteRequest.getWorkSpaceId());
        cardRepository.deleteById(cardId);
    }

    private void checkMemberRole(Long userId, Long workSpaceId) {
        // 읽기 전용 역할인 경우 예외 발생
        if (memberRepository.findByUserIdAndWorkSpaceId(userId, workSpaceId) == null) {
            throw new ApiException(ErrorStatus._READ_ONLY_ROLE);
        }
    }
}
