package com.javastart.accountservice.controller.dto;


import com.javastart.accountservice.entity.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountResponseDto {

    private Long accountId;

    private String name;

    private String email;

    private String phone;

    private OffsetDateTime creationDate;

    private List<Long> bills;

    public AccountResponseDto(Account account) {
        this.accountId = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.bills = account.getBills();
        this.creationDate = account.getCreationDate();
    }
}
