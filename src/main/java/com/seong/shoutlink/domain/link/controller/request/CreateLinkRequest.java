package com.seong.shoutlink.domain.link.controller.request;

import jakarta.validation.constraints.NotBlank;

public record CreateLinkRequest(
    @NotBlank(message = "링크 url은 필수값입니다.")
    String url,
    String description) {

}
