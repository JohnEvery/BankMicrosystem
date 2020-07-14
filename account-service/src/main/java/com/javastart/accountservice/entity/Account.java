package com.javastart.accountservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String email;

    private String phone;

    private OffsetDateTime creationDate;

    @ElementCollection
    private List<Long> bills;

    public Account(String name, String email, String phone, List<Long> bills, OffsetDateTime creationDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bills = bills;
        this.creationDate = creationDate;
    }
}
