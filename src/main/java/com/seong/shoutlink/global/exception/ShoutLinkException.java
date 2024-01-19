package com.seong.shoutlink.global.exception;

import lombok.Getter;

@Getter
public class ShoutLinkException extends RuntimeException {

    private final ErrorCode errorCode;

    public ShoutLinkException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
