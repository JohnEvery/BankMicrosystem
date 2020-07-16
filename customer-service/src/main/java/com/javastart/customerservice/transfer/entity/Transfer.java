package com.javastart.customerservice.transfer.entity;

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
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private OffsetDateTime transferDate;

    private Long billFrom;

    private Long billTo;

    private String emailFrom;

    private String emailTo;

    private BigDecimal amount;

    public Transfer(OffsetDateTime transferDate, Long billFrom, Long billTo, String emailFrom, String emailTo, BigDecimal amount) {
        this.transferDate = transferDate;
        this.billFrom = billFrom;
        this.billTo = billTo;
        this.emailFrom = emailFrom;
        this.emailTo = emailTo;
        this.amount = amount;
    }
}
