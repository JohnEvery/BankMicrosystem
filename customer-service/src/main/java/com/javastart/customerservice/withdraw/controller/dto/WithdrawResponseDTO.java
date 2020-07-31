package com.javastart.customerservice.withdraw.controller.dto;

import com.javastart.customerservice.withdraw.entity.Withdraw;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class WithdrawResponseDTO {

//    private OffsetDateTime withdrawDate;

    private Long billId;

    private String accountEmail;

    private BigDecimal amount;

    private BigDecimal balance;

    public WithdrawResponseDTO(Withdraw withdraw) {
//        this.withdrawDate = withdraw.getWithdrawTime();
        this.billId = withdraw.getBillId();
        this.accountEmail = withdraw.getEmail();
        this.amount = withdraw.getAmount();
        this.balance = withdraw.getBalance();
    }
}
