package com.javastart.notificationservice.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponseDTO implements Serializable {

    public static final int serialVersionUID = 13;

    private Long billIdFrom;

    private Long billIdTo;

    private String accountEmailFrom;

    private String accountEmailTo;

    private BigDecimal amount;
}
