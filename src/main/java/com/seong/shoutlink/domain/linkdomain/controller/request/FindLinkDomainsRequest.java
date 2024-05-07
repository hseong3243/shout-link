package com.seong.shoutlink.domain.linkdomain.controller.request;

import jakarta.validation.constraints.Min;
import java.util.Objects;

public record FindLinkDomainsRequest(
    String keyword,
    @Min(value = 0, message = "페이지는 음수일 수 없습니다.")
    Integer page,
    @Min(value = 1, message = "사이즈는 1이상이어야 합니다.")
    Integer size) {

    public FindLinkDomainsRequest(String keyword, Integer page, Integer size) {
        this.keyword = Objects.isNull(keyword) ? "" : keyword;
        this.page = page;
        this.size = size;
    }
}
