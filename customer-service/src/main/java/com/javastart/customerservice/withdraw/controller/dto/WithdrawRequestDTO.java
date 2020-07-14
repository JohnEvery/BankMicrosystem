package com.javastart.customerservice.withdraw.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequestDTO {

    private Long billId;

    private Long accountId;

    private BigDecimal amount;
}
