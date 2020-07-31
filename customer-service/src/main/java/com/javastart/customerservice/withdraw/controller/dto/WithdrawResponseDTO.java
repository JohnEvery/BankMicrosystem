package com.javastart.customerservice.withdraw.controller.dto;

import com.javastart.customerservice.withdraw.entity.Withdraw;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class WithdrawResponseDTO {

    private String withdrawDate;

    private Long billId;

    private String accountEmail;

    private BigDecimal amount;

    private BigDecimal balance;

    public WithdrawResponseDTO(Withdraw withdraw) {
        this.withdrawDate = withdraw.getWithdrawTime().toString();
        this.billId = withdraw.getBillId();
        this.accountEmail = withdraw.getEmail();
        this.amount = withdraw.getAmount();
        this.balance = withdraw.getBalance();
    }
}
