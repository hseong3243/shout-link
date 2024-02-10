package com.seong.shoutlink.domain.hub.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CreateHubRequest(
    @NotBlank(message = "허브 이름은 공백일 수 없습니다.")
    String name,
    String description) {

}
