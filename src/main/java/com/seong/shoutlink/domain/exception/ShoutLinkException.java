package com.seong.shoutlink.domain.exception;

import lombok.Getter;

@Getter
public class ShoutLinkException extends RuntimeException {

    private final ErrorCode errorCode;

    public ShoutLinkException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
