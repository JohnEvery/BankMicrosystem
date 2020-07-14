package com.javastart.customerservice.deposit.repository;

import com.javastart.customerservice.deposit.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DepositRepository extends JpaRepository<Deposit, Long> {

    Optional<List<Deposit>> findAllByEmail(String email);

    Optional<List<Deposit>> findAllByBillId(Long billId);
}
