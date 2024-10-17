package io.rednotice.workspace.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddMemberRequest {

    private String email;
    private String memberRole;
}
