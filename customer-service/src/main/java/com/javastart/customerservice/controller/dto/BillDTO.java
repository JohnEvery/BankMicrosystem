package com.javastart.customerservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
public class BillDTO {

    private Long accountId;

    private Boolean isDefault;

    private BigDecimal amount;

    private Boolean overdraftEnabled;
}
