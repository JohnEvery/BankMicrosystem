package com.javastart.billservice.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExceptionArgumentsResponseTemplate {

    private String message;

    private int status;

    private String exception;

    private List<String> errorArguments;
}
