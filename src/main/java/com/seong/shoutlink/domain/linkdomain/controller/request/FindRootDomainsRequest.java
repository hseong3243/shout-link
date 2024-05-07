package com.seong.shoutlink.domain.linkdomain.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

public record FindRootDomainsRequest(
    @NotNull(message = "검색어는 필수입니다.")
    @Size(min = 1, message = "검색어는 1자 이상이어야 합니다.")
    String keyword,
    Integer size) {

    public FindRootDomainsRequest(String keyword, Integer size) {
        this.keyword = keyword;
        this.size = Objects.isNull(size) ? 10 : size;
    }
}
