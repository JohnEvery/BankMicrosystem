package com.javastart.customerservice.rest;

import com.javastart.customerservice.exceptions.UnableValueForRequestException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CustomerRestService {

    public Boolean checkAmount (BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) > 0) {
            return true;
        } else {
            throw new UnableValueForRequestException("Unable amount value");
        }
    }
}
