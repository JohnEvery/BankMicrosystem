package com.javastart.notificationservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawResponseDTO implements Serializable {

    public static final int serialVersionUID = 11;

    private String withdrawDate;

    private Long billId;

    private String accountEmail;

    private BigDecimal amount;

    private BigDecimal balance;
}
