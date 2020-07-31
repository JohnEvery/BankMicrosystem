package com.javastart.notificationservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponseDTO implements Serializable {

    public static final int serialVersionUID = 10;

//    private OffsetDateTime depositDate;

    private Long billId;

    private String email;

    private BigDecimal amount;

    private BigDecimal balance;
}
