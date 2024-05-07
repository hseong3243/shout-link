package com.seong.shoutlink.domain.linkdomain.controller.request;

import jakarta.validation.constraints.Min;
import java.util.Objects;
import org.hibernate.validator.constraints.Range;

public record FindLinkDomainLinksRequest(
    @Min(value = 0, message = "페이지는 음수일 수 없습니다.")
    Integer page,
    @Range(min = 1, max = 100, message = "사이즈는 1 이상 100 이하여야 합니다.")
    Integer size) {

    public FindLinkDomainLinksRequest(Integer page, Integer size) {
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 10 : size;
    }
}
