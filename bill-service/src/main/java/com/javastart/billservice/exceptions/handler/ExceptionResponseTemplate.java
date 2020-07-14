package com.javastart.billservice.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ExceptionResponseTemplate {

    private OffsetDateTime time;

    private String message;

    private int status;

    private String exception;
}
