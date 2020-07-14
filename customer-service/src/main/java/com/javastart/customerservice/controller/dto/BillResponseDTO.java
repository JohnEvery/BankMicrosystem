package com.javastart.customerservice.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class BillResponseDTO {

    private Long billId;

    private Long accountId;

    private BigDecimal amount;

    private Boolean overdraftEnabled;

    private Boolean isDefault;

    private OffsetDateTime createDate;
}
