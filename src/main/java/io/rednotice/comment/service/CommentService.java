package io.rednotice.comment.service;

import io.rednotice.card.entity.Card;
import io.rednotice.card.service.CardService;
import io.rednotice.comment.entity.Comment;
import io.rednotice.comment.repository.CommentRepository;
import io.rednotice.comment.request.CommentRequest;
import io.rednotice.comment.request.CommentUpdateRequest;
import io.rednotice.comment.response.CommentResponse;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import io.rednotice.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardService cardService;
    private final MemberService memberService;

    @Transactional
    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {
        validRole(authUser.getId(), request.getCardId());

        Comment comment = new Comment(request.getContent());
        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    @Transactional
    public CommentResponse updateComment(AuthUser authUser, CommentUpdateRequest request, Long commentId) {
        checkCommentOwner(authUser.getId(), commentId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        comment.changeContent(request.getContent());

        return CommentResponse.of(comment);
    }

    @Transactional
    public void deleteComment(AuthUser authUser, Long id) {
        checkCommentOwner(authUser.getId(), id);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        commentRepository.delete(comment);
    }

    public void validRole(Long userId, Long cardId) {
        Card card = cardService.getCard(cardId);
        memberService.checkReadAndWrite(userId, card.getWorkspace().getId());
    }

    public void checkCommentOwner(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getId().equals(userId)) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }
}
