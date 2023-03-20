package com.example.dodam.common.advice;

import com.example.dodam.common.dto.ErrorResponse;
import com.example.dodam.common.exception.RegisterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleRegisterException(RegisterException e) {
        ErrorResponse response = new ErrorResponse(e.getErrorCode().getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
