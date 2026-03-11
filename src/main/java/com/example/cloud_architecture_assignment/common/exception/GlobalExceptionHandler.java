package com.example.cloud_architecture_assignment.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("[API - LOG] 멤버 생성 에러 발생 - ID: ", e);
        return ResponseEntity.status(e.getStatusCode()).body(
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(ResponseStatusException e) {
        log.error("[API - LOG] 조회 에러 발생 - ID: ", e);
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }


}
