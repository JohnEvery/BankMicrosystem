package com.javastart.customerservice.exceptions;

public class UnableValueForRequestException extends RuntimeException{

    public UnableValueForRequestException(String message) {
        super(message);
    }
}
