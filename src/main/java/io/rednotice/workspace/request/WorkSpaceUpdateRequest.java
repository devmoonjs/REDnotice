package io.rednotice.workspace.request;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkSpaceUpdateRequest {

    @Nullable
    private String name;

    @Nullable
    private String description;
}
