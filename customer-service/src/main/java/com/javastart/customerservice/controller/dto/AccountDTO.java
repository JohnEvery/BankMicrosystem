package com.javastart.customerservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AccountDTO {

    private String name;

    private String email;

    private String phone;

    private List<Long> bills;
}
