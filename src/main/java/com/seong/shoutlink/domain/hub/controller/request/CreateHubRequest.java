package com.seong.shoutlink.domain.hub.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateHubRequest(
    @NotBlank(message = "허브 이름은 공백일 수 없습니다.")
    String name,
    String description,
    @NotNull(message = "허브 공개 여부는 필수입니다.")
    Boolean isPrivate) {

}
