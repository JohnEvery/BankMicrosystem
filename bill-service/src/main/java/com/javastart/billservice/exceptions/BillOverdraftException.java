package com.javastart.billservice.exceptions;

public class BillOverdraftException extends RuntimeException {
    public BillOverdraftException(String message) {
        super(message);
    }
}
