package com.javastart.customerservice.deposit.controller.dto;

import com.javastart.customerservice.deposit.entity.Deposit;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class DepositResponseDTO {

    private String depositDate;

    private Long billId;

    private String email;

    private BigDecimal amount;

    private BigDecimal balance;

    public DepositResponseDTO(Deposit deposit) {
        this.depositDate = deposit.getDepositTime().toString();
        this.billId = deposit.getBillId();
        this.email = deposit.getEmail();
        this.amount = deposit.getAmount();
        this.balance = deposit.getBalance();
    }

    public DepositResponseDTO(Deposit deposit, BigDecimal balance) {
        this.depositDate = deposit.getDepositTime().toString();
        this.billId = deposit.getBillId();
        this.email = deposit.getEmail();
        this.amount = deposit.getAmount();
        this.balance = balance;
    }
}
