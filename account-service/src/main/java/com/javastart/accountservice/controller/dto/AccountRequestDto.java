package com.javastart.accountservice.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountRequestDto {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String phone;

    private List<Long> bills;
}
