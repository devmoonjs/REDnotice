package io.rednotice.card.controller;

import io.rednotice.card.dto.request.*;
import io.rednotice.card.dto.response.CardDetailResponse;
import io.rednotice.card.dto.response.CardManagerResponse;
import io.rednotice.card.dto.response.CardResponse;
import io.rednotice.card.dto.CardSearchDto;
import io.rednotice.card.service.CardService;
import io.rednotice.card.dto.request.CardManagerRequest;
import io.rednotice.common.AuthUser;
import io.rednotice.common.anotation.SlackNotify;
import io.rednotice.common.apipayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CardController {
    private final CardService cardService;

    @PostMapping("/cards")
    public ApiResponse<CardResponse> saveCard(@AuthenticationPrincipal AuthUser authUser,
                                              @RequestBody CardSaveRequest cardSaveRequest) {
        return ApiResponse.ok(cardService.saveCard(authUser, cardSaveRequest));
    }

    @PostMapping("/cards/{cardId}/managers")
    public ApiResponse<CardManagerResponse> addCardManager(@AuthenticationPrincipal AuthUser authUser,
                                                           @PathVariable Long cardId,
                                                           @RequestBody CardManagerRequest cardManagerRequest) {
        return ApiResponse.ok(cardService.changeManager(authUser, cardId, cardManagerRequest));
    }

    @GetMapping("/cards")
    public ApiResponse<Page<CardSearchDto>> searchCards(@ParameterObject CardPageRequest cardPageRequest,
                                                        @ParameterObject CardSearchRequest cardSearchRequest) {
        return ApiResponse.ok(cardService.searchCards(cardPageRequest, cardSearchRequest));
    }

    @GetMapping("/cards/{cardId}")
    public ApiResponse<CardDetailResponse> getCard(@PathVariable Long cardId) {
        return ApiResponse.ok(cardService.getCard(cardId));
    }

    @GetMapping("/cards/top10")
    public ApiResponse<List<CardResponse>> getTopRankedCards() {
        return ApiResponse.ok(cardService.getTopRankedCards());
    }

    @SlackNotify
    @PatchMapping("/cards/{cardId}")
    public ApiResponse<CardResponse> updateCard(@AuthenticationPrincipal AuthUser authUser,
                                                @PathVariable Long cardId,
                                                @RequestBody CardUpdateRequest cardUpdateRequest) {
        return ApiResponse.ok(cardService.updateCard(authUser, cardId, cardUpdateRequest));
    }

    @DeleteMapping("/cards/{cardId}")
    public ApiResponse<String> deleteCard(@AuthenticationPrincipal AuthUser authUser,
                                        @PathVariable Long cardId,
                                        @RequestBody CardDeleteRequest cardDeleteRequest) {
        cardService.deleteCard(authUser, cardId, cardDeleteRequest);
        return ApiResponse.ok("카드가 삭제되었습니다.");
    }

    // 카드 순서, list 이동은 일단 보류
}
