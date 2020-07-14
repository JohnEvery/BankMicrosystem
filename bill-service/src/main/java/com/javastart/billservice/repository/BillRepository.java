package com.javastart.billservice.repository;

import com.javastart.billservice.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Long> {

    Optional<List<Bill>> findBillsByAccountId(Long accountId);
}
