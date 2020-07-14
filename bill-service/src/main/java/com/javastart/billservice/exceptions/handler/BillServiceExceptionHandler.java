package com.javastart.billservice.exceptions.handler;

import com.javastart.billservice.exceptions.BillNotFoundException;
import com.javastart.billservice.exceptions.BillOverdraftException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class BillServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BillNotFoundException.class})
    public ResponseEntity<ExceptionResponseTemplate> handleBillNotFoundException(BillNotFoundException ex) {

        return new ResponseEntity<>(new ExceptionResponseTemplate(OffsetDateTime.now(), ex.getMessage(), 404,
                "NotFoundException"), HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        List<String> errors = allErrors.stream().map(objectError
                -> ((FieldError) objectError).getField()).collect(Collectors.toList());
        String message = "Validation failed for request params";
        return new ResponseEntity<>(new ExceptionArgumentsResponseTemplate(message, 400,
                "ArgumentNotValidException", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BillOverdraftException.class})
    public ResponseEntity<ExceptionResponseTemplate> handleBillOverdraftException
            (BillOverdraftException ex) {
        return new ResponseEntity<>(new ExceptionResponseTemplate(OffsetDateTime.now(), ex.getMessage(),
                400, "OverdraftBillException"), HttpStatus.BAD_REQUEST);
    }
}
