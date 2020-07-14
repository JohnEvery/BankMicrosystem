package com.javastart.customerservice.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountRequestDTO {

    private String name;

    private String email;

    private String phone;
}
