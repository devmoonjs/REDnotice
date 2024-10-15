package io.rednotice.workspace.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WorkSpaceNameResponse {

    private final String name;

    public static WorkSpaceNameResponse of (String name) {
        return new WorkSpaceNameResponse(
                name
        );
    }
}
