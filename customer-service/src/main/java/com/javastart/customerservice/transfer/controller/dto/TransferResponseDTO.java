package com.javastart.customerservice.transfer.controller.dto;

import com.javastart.customerservice.transfer.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class TransferResponseDTO {

//    private OffsetDateTime transferDate;

    private Long billIdFrom;

    private Long billIdTo;

    private String accountEmailFrom;

    private String accountEmailTo;

    private BigDecimal amount;

    public TransferResponseDTO(Transfer transfer) {
//        this.transferDate = transfer.getTransferDate();
        this.billIdFrom = transfer.getBillFrom();
        this.billIdTo = transfer.getBillTo();
        this.accountEmailFrom = transfer.getEmailFrom();
        this.accountEmailTo = transfer.getEmailTo();
        this.amount = transfer.getAmount();
    }
}
