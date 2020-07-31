package com.javastart.customerservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMessageResponseDTO implements Serializable {

    private static final int serialVersionUID = 14;

    private String name;

    private String email;

    private String phone;
}
