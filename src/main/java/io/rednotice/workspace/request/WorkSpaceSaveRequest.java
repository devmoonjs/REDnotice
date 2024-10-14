package io.rednotice.workspace.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkSpaceSaveRequest {

    private String name;
    private String description;
}
