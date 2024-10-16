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
import io.rednotice.member.entity.Member;
import io.rednotice.member.repository.MemberRepository;
import io.rednotice.workspace.entity.WorkSpace;
import io.rednotice.workspace.enums.MemberRole;
import io.rednotice.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;
    private final WorkSpaceRepository workSpaceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {

        validRole(authUser.getId(), request.getCardId());

        Comment comment = new Comment(request.getContent());
        commentRepository.save(comment);

        return CommentResponse.of(comment);
    }

    public CommentResponse updateComment(AuthUser authUser, CommentUpdateRequest request, Long commentId) {

        isCommentOwner(authUser.getId(), commentId);

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        comment.changeContent(request.getContent());

        return CommentResponse.of(comment);
    }

    public void deleteComment(AuthUser authUser, Long id) {

        isCommentOwner(authUser.getId(), id);

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        commentRepository.delete(comment);
    }

    public void validRole(Long userId, Long cardId) {

        Card card = cardRepository.getCardById(cardId);

        Member member = memberRepository.findByUserIdAndWorkspaceId(userId, card.getWorkspace().getId()).orElseThrow(
                () -> new ApiException(ErrorStatus._PERMISSION_DENIED)
        );

        if (member.getMemberRole() == MemberRole.READ) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }

    public void isCommentOwner(Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ApiException(ErrorStatus._NOT_FOUND_COMMENT)
        );

        if (!comment.getUser().getId().equals(userId)) {
            throw new ApiException(ErrorStatus._PERMISSION_DENIED);
        }
    }
}
