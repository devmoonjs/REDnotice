package io.rednotice.workspace.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkSpaceUpdateRequest {

    @Nullable
    private String name;

    @Nullable
    private String description;
}
