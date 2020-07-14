package com.javastart.billservice.entity;


import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long accountId;

    private Boolean isDefault;

    private BigDecimal amount;

    private Boolean overdraftEnabled;

    private OffsetDateTime createDate;

    public Bill(Long accountId, Boolean isDefault, BigDecimal amount, Boolean overdraftEnabled, OffsetDateTime createDate) {
        this.accountId = accountId;
        this.isDefault = isDefault;
        this.amount = amount;
        this.overdraftEnabled = overdraftEnabled;
        this.createDate = createDate;
    }
}
