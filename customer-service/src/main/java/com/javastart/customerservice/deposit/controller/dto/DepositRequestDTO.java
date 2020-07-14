package com.javastart.customerservice.deposit.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequestDTO {

    private Long accountId;

    private Long billId;

    private BigDecimal amount;
}
