package io.rednotice.workspace.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AddMemberRequest {

    private String email;
    private String memberRole;
}
