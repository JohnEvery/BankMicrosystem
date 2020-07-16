package com.javastart.customerservice.transfer.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class TransferRequestDTO {

    private Long billIdFrom;

    private Long billIdTo;

    private Long accountIdFrom;

    private Long accountIdTo;

    private BigDecimal amount;
}
