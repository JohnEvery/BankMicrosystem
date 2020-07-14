package com.javastart.customerservice.withdraw.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Data
@NoArgsConstructor
public class Withdraw {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OffsetDateTime withdrawTime;

    private Long billId;

    private String email;

    private BigDecimal amount;

    public Withdraw(OffsetDateTime withdrawTime, Long billId, String email, BigDecimal amount) {
        this.withdrawTime = withdrawTime;
        this.billId = billId;
        this.email = email;
        this.amount = amount;
    }
}
