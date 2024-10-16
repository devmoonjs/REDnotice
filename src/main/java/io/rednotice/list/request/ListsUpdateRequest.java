package io.rednotice.list.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListsUpdateRequest {
    @Size(max = 255, message = "리스트 이름은 255자 이내여야 합니다.")
    private String name;

    @Positive(message = "리스트 순서는 1 이상의 숫자여야 합니다.")
    private int sequence;

    @NotNull(message = "워크스페이스 ID는 필수입니다.")
    private Long workSpaceId;
}
