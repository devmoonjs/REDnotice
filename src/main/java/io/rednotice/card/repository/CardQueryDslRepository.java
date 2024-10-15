package io.rednotice.card.repository;

import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.dto.request.CardSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CardQueryDslRepository {
    Page<CardSearchDto> search(String title,
                               String description,
                               LocalDate dueDate,
                               String managerName,
                               Long boardId,
                               Pageable pageable);
}
