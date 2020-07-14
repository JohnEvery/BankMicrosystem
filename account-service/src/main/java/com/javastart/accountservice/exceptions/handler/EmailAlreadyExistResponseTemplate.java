package com.javastart.accountservice.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class EmailAlreadyExistResponseTemplate {

    private OffsetDateTime time;

    private String message;

    private int status;

    private String exception;
}
