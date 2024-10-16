package io.rednotice.list.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsSaveRequest {
    private String name;
    private int sequence;
    private Long workSpaceId;
    private Long boardId;
}
