package com.javastart.customerservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillRequestDTO {

    private Long billId;

    private Boolean isDefault;

    private BigDecimal amount;

    private Boolean overdraftEnabled;
}
