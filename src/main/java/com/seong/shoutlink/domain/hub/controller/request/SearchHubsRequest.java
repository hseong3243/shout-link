package com.seong.shoutlink.domain.hub.controller.request;

import jakarta.validation.constraints.Min;
import java.util.Objects;
import org.hibernate.validator.constraints.Range;

public record SearchHubsRequest(
    String tagKeyword,
    @Min(value = 0, message = "페이지는 0 이상이어야 합니다.")
    Integer page,
    @Range(min = 0, max = 100, message = "사이즈는 0 이상, 100 이하여야 합니다.")
    Integer size) {

    public SearchHubsRequest(String tagKeyword, Integer page, Integer size) {
        this.tagKeyword = Objects.isNull(tagKeyword) ? "" : tagKeyword;
        this.page = Objects.isNull(page) ? 0 : page;
        this.size = Objects.isNull(size) ? 20 : size;
    }
}
