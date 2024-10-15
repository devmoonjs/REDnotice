package io.rednotice.card.repository;

import io.rednotice.card.entity.Card;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long>{

    default Card getCardById(Long cardId) {
        return findById(cardId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_CARD)
        );
    }
}
