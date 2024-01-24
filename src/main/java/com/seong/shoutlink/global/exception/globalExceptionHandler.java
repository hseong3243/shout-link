package com.seong.shoutlink.global.exception;

import com.seong.shoutlink.domain.exception.ShoutLinkException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(ShoutLinkException.class)
    public ResponseEntity<ErrorResponse> shoutLinkExHandle(ShoutLinkException e) {
        ErrorResponse errorResponse
            = new ErrorResponse(e.getMessage(), e.getErrorCode().getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
    }
}
