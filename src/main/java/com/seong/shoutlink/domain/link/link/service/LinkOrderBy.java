package com.seong.shoutlink.domain.link.link.service;

import lombok.Getter;

@Getter
public enum LinkOrderBy {
    CREATED_AT("createdAt"),
    EXPIRED_AT("expiredAt");

    private final String orderBy;

    LinkOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
