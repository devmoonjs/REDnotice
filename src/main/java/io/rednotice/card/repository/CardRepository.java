package io.rednotice.card.repository;

import io.rednotice.card.entity.Card;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>, CardQueryDslRepository{

    default Card findCardById(Long cardId) {
        return findById(cardId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_CARD)
        );
    }

    default List<Card> findCardsByIds(List<Long> cardIds){
        return findAllById(cardIds);
    }
}
