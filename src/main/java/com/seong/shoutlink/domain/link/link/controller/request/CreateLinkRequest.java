package com.seong.shoutlink.domain.link.link.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateLinkRequest(
    @NotNull(message = "링크 묶음 ID는 필수값입니다.")
    Long linkBundleId,
    @NotBlank(message = "링크 url은 필수값입니다.")
    String url,
    String description,
    LocalDateTime expiredAt) {

}
