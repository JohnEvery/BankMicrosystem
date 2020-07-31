package com.javastart.notificationservice.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDTO implements Serializable {

    private static final int serialVersionUID = 14;

    private String name;

    private String email;

    private String phone;
}
