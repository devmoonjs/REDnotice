package io.rednotice.list.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsSaveRequest {
    private final String name;
    private final int sequence;
    private Long boardId;
}
