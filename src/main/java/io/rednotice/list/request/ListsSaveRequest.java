package io.rednotice.list.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsSaveRequest {
    private Long boardId;
    private Long workSpaceId;
    private String name;
    private int sequence;
}
