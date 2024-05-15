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
    UNAUTHORIZED("SL301", Constants.FORBIDDEN),
    NOT_FOUND("SL401", Constants.NOT_FOUND),
    DUPLICATE_EMAIL("SL901", Constants.CONFLICT),
    DUPLICATE_NICKNAME("SL902", Constants.BAD_REQUEST),
    NOT_MET_CONDITION("SL1001", Constants.BAD_REQUEST),
    SERVER_ERROR("SL9999", Constants.SERVER_ERROR);

    private final String errorCode;
    private final int status;

    private static class Constants {

        private static final int BAD_REQUEST = 400;
        private static final int UNAUTHORIZED = 401;
        private static final int FORBIDDEN = 401;
        private static final int NOT_FOUND = 404;
        private static final int CONFLICT = 409;
        private static final int SERVER_ERROR = 500;
    }
}
