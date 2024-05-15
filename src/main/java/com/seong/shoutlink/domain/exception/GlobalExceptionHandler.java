package com.seong.shoutlink.domain.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ShoutLinkException.class)
    public ResponseEntity<ErrorResponse> shoutLinkExHandle(ShoutLinkException e) {
        ErrorResponse errorResponse
            = new ErrorResponse(e.getMessage(), e.getErrorCode().getErrorCode());
        return ResponseEntity.status(e.getErrorCode().getStatus()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exHandle(Exception e) {
        log.error("예측하지 못한 예외 발생, 메세지={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("서버 에러가 발생했습니다.", ErrorCode.SERVER_ERROR.getErrorCode()));
    }
}
