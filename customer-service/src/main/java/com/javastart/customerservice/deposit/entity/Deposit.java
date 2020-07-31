package com.javastart.customerservice.deposit.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
public class Deposit implements Serializable {

    public static final int serialVersionUID = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OffsetDateTime depositTime;

    private Long billId;

    private String email;

    private BigDecimal amount;

    private BigDecimal balance;

    public Deposit(OffsetDateTime depositDate, Long billId, String email, BigDecimal amount, BigDecimal balance) {
        this.depositTime = depositDate;
        this.billId = billId;
        this.email = email;
        this.amount = amount;
        this.balance = balance;
    }
}
