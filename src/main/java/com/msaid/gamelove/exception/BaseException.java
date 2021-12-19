package com.msaid.gamelove.exception;

public class BaseException extends RuntimeException{
    public BaseException(String message) {
        super(message);
    }
    public BaseException() {
        super();
    }
}
