package io.rednotice.workspace.response;

import io.rednotice.workspace.entity.WorkSpace;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkSpaceResponse {

    private final String name;
    private final String description;

    public static WorkSpaceResponse of(WorkSpace workSpace) {
        return new WorkSpaceResponse(
                workSpace.getName(),
                workSpace.getDescription()
        );
    }
}
