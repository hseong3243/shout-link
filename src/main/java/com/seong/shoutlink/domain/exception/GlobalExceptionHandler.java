package com.seong.shoutlink.domain.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShoutLinkException.class)
    public ResponseEntity<ErrorResponse> shoutLinkExHandle(ShoutLinkException e) {
        ErrorResponse errorResponse
            = new ErrorResponse(e.getMessage(), e.getErrorCode().getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
    }
}
