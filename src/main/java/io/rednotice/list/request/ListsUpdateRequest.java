package io.rednotice.list.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsUpdateRequest {
    private String name;
    private int sequence;
}
