package com.msaid.gamelove.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<String>> handleException(WebExchangeBindException e) {
        List<String> errors = e.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleGeneralException(Throwable throwable){
        return ResponseEntity.badRequest()
                .body(throwable.getLocalizedMessage());
    }

    // todo can be customized
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<String> handleBaseException(BaseException exception){
        return handleGeneralException(exception);
    }
}
