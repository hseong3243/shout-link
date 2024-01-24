package com.seong.shoutlink.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    ILLEGAL_ARGUMENT("SL001", Constants.BAD_REQUEST),
    UNAUTHENTICATED("SL101", Constants.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN("SL102", Constants.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("SL103", Constants.UNAUTHORIZED),
    DUPLICATE_EMAIL("SL901", Constants.CONFLICT),
    DUPLICATE_NICKNAME("SL902", Constants.BAD_REQUEST);

    private final String errorCode;
    private final int status;

    private static class Constants {

        private static final int BAD_REQUEST = 400;
        private static final int UNAUTHORIZED = 401;
        private static final int CONFLICT = 409;
    }
}
