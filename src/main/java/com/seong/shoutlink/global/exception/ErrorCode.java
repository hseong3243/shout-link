package com.seong.shoutlink.global.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT("SL001"),
    UNAUTHENTICATED("SL401"),
    DUPLICATE_EMAIL("SL901"),
    DUPLICATE_NICKNAME("SL902");

    private final String code;
}
