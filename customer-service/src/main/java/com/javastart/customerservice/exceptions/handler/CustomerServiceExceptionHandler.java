package com.javastart.customerservice.exceptions.handler;

import com.javastart.customerservice.exceptions.DepositException;
import com.javastart.customerservice.exceptions.UnableValueForRequestException;
import com.javastart.customerservice.exceptions.WithdrawException;
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
public class CustomerServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UnableValueForRequestException.class})
    public ResponseEntity<ExceptionResponseTemplate> unableValueForRequestException(UnableValueForRequestException ex) {
        return new ResponseEntity<>(new ExceptionResponseTemplate(OffsetDateTime.now(), ex.getMessage(), 400,
                "UnableValueForRequestException"), HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler({DepositException.class})
    public ResponseEntity<ExceptionResponseTemplate> depositException(DepositException ex) {
        return new ResponseEntity<>(new ExceptionResponseTemplate(OffsetDateTime.now(), ex.getMessage(),
                400, "DepositException"), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({WithdrawException.class})
    public ResponseEntity<ExceptionResponseTemplate> withdrawException(WithdrawException ex) {
        return new ResponseEntity<>(new ExceptionResponseTemplate(OffsetDateTime.now(), ex.getMessage(),
                400, "WithdrawException"), HttpStatus.CONFLICT);
    }
}
