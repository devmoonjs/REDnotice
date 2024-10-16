package io.rednotice.comment.service;

import io.rednotice.card.entity.Card;
import io.rednotice.card.repository.CardRepository;
import io.rednotice.comment.entity.Comment;
import io.rednotice.comment.repository.CommentRepository;
import io.rednotice.comment.request.CommentRequest;
import io.rednotice.comment.request.CommentUpdateRequest;
import io.rednotice.comment.response.CommentResponse;
import io.rednotice.common.AuthUser;
import io.rednotice.common.apipayload.status.ErrorStatus;
import io.rednotice.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    /*
        Card 에 WorkspaceId 추가 하고 수정 예정
     */
    @Transactional
    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {

        Comment comment = new Comment(request.getContent());
        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public CommentResponse updateComment(AuthUser authUser, CommentUpdateRequest request, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        comment.changeContent(request.getContent());

        return CommentResponse.of(comment);
    }
}
