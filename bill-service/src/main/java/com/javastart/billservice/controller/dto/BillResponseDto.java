package com.javastart.billservice.controller.dto;

import com.javastart.billservice.entity.Bill;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
public class BillResponseDto {

    private Long accountId;

    private Long billId;

    private Boolean isDefault;

    private BigDecimal amount;

    private Boolean overdraftEnabled;

    private OffsetDateTime createDate;

    public BillResponseDto(Bill bill) {
        accountId = bill.getAccountId();
        billId = bill.getId();
        isDefault = bill.getIsDefault();
        amount = bill.getAmount();
        overdraftEnabled = bill.getOverdraftEnabled();
        createDate = bill.getCreateDate();
    }
}
