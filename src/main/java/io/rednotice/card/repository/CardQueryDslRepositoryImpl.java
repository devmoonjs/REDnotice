package io.rednotice.card.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.dto.QCardSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import static io.rednotice.board.entity.QBoard.board;
import static io.rednotice.card.entity.QCard.card;
import static io.rednotice.list.entity.QLists.lists;
import static io.rednotice.user.entity.QUser.user;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CardQueryDslRepositoryImpl implements CardQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CardSearchDto> search(String title,
                                      String description,
                                      LocalDate dueDate,
                                      String managerName,
                                      Long boardId,
                                      Pageable pageable) {
        List<CardSearchDto> results = queryFactory
                .select(
                    new QCardSearchDto(
                            card.id,
                            card.title,
                            card.description,
                            card.dueDate,
                            card.board.id,
                            card.createdAt,
                            card.modifiedAt
                    )
                )
                .from(card)
                .leftJoin(card.board, board)
                .leftJoin(card.list, lists)
                .leftJoin(card.user, user)
                .where(
                        titleContains(title),
                        descriptionContains(description),
                        dueDateIsEq(dueDate),
                        managerNameIsEq(managerName),
                        boardIdIsEq(boardId)
                )
                .groupBy(card.id)
                .orderBy(card.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(card)
                .where(
                        titleContains(title),
                        descriptionContains(description),
                        dueDateIsEq(dueDate),
                        managerNameIsEq(managerName),
                        boardIdIsEq(boardId)
                ).fetchOne();

        return new PageImpl<>(results, pageable, totalCount);
    }


    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? card.title.contains(title) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return StringUtils.hasText(description) ? card.description.contains(description) : null;
    }

    private BooleanExpression dueDateIsEq(LocalDate dueDate) {
        return dueDate != null ? card.dueDate.eq(dueDate) : null;
    }

    private BooleanExpression managerNameIsEq(String managerName) {
        return StringUtils.hasText(managerName) ? card.manager.username.eq(managerName) : null;
    }

    private BooleanExpression boardIdIsEq(Long boardId) {
        return card.board != null ? card.board.id.eq(boardId) : null;
    }

}
