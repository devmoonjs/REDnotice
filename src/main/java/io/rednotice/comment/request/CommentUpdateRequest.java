package io.rednotice.comment.request;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    private Long cardId;
    private String content;
}
