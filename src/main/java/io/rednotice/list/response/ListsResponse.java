package io.rednotice.list.response;

import io.rednotice.list.entity.Lists;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListsResponse {
    private final String name;
    private final int sequence;

    public static ListsResponse of(Lists lists) {
        return new ListsResponse(
                lists.getName(),
                lists.getSequence()
        );
    }
}
