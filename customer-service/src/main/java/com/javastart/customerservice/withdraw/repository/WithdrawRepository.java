package com.javastart.customerservice.withdraw.repository;

import com.javastart.customerservice.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {

    Optional<List<Withdraw>> getByEmail(String accountEmail);

    Optional<List<Withdraw>> getByBillId(Long billId);
}
